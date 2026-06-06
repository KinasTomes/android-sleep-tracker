"""Chronos-2 sleep duration prediction.

Uses Amazon's Chronos-2 pre-trained foundation model for zero-shot
time series forecasting. No training required — feeds recent sleep
history and returns probabilistic predictions with confidence intervals.
"""

import logging
from dataclasses import dataclass
from datetime import date

import pandas as pd

from app.core.config import settings

logger = logging.getLogger(__name__)

# Singleton pipeline — loaded once on first use
_pipeline = None


def _get_pipeline():
    """Lazy-load Chronos-2 pipeline (singleton)."""
    global _pipeline
    if _pipeline is None:
        try:
            from chronos import Chronos2Pipeline

            logger.info("Loading Chronos-2 model: %s", settings.CHRONOS_MODEL_ID)
            _pipeline = Chronos2Pipeline.from_pretrained(
                settings.CHRONOS_MODEL_ID,
                device_map=settings.CHRONOS_DEVICE,
            )
            logger.info("Chronos-2 model loaded successfully")
        except Exception:
            logger.exception("Failed to load Chronos-2 model")
            _pipeline = None
    return _pipeline


@dataclass
class PredictionResult:
    predicted_hours: float  # median (50th percentile)
    ci_low: float  # 10th percentile
    ci_high: float  # 90th percentile


@dataclass(frozen=True)
class SleepHistoryPoint:
    sleep_date: date
    hours: float


def predict_sleep_duration(
    sleep_history: list[SleepHistoryPoint],
    prediction_length: int = 1,
) -> PredictionResult | None:
    """Predict next night's sleep duration from recent history.

    Args:
        sleep_history: Recent sleep history ordered chronologically. Each point
                       includes a sleep date and total sleep duration in hours.
                       Minimum 3 values recommended.
        prediction_length: Number of future nights to predict (default 1).

    Returns:
        PredictionResult with median, 10th/90th percentile, or None if model unavailable.
    """
    pipeline = _get_pipeline()
    if pipeline is None:
        logger.warning("Chronos-2 model not available, returning None")
        return None

    try:
        regular_history = _regularize_daily_history(sleep_history)
        # Build context DataFrame — Chronos-2 expects a DataFrame with
        # an 'id' column and a target column
        context_df = pd.DataFrame({
            "id": ["sleep"] * len(regular_history),
            "timestamp": pd.to_datetime([point.sleep_date for point in regular_history]),
            "target": [point.hours for point in regular_history],
        })

        # Predict with quantile levels for confidence interval
        pred_df = pipeline.predict_df(
            context_df,
            prediction_length=prediction_length,
            quantile_levels=[0.1, 0.5, 0.9],
            id_column="id",
            timestamp_column="timestamp",
            target="target",
        )

        # Extract results from the prediction DataFrame
        # pred_df has columns: id, timestamp, 0.1, 0.5, 0.9
        row = pred_df.iloc[0]
        ci_low = _get_quantile(row, 0.1)
        median = _get_quantile(row, 0.5)
        ci_high = _get_quantile(row, 0.9)

        # Clamp to reasonable bounds (0-24 hours)
        ci_low = max(0.0, min(24.0, ci_low))
        median = max(0.0, min(24.0, median))
        ci_high = max(0.0, min(24.0, ci_high))

        return PredictionResult(
            predicted_hours=round(median, 2),
            ci_low=round(ci_low, 2),
            ci_high=round(ci_high, 2),
        )

    except Exception:
        logger.exception("Chronos-2 prediction failed")
        return None


def _regularize_daily_history(sleep_history: list[SleepHistoryPoint]) -> list[SleepHistoryPoint]:
    """Return a daily-frequency history for Chronos dataframe validation.

    Chronos-2 infers frequency from timestamps and rejects irregular dates. Missing
    sleep dates are interpolated so recording gaps do not get treated as zero sleep.
    """
    if not sleep_history:
        return []

    series = pd.Series(
        data=[point.hours for point in sleep_history],
        index=pd.to_datetime([point.sleep_date for point in sleep_history]),
        dtype="float64",
    ).sort_index()
    series = series[~series.index.duplicated(keep="last")]
    regular = series.asfreq("D").interpolate(method="time").ffill().bfill()

    return [
        SleepHistoryPoint(sleep_date=timestamp.date(), hours=float(hours))
        for timestamp, hours in regular.items()
    ]


def _get_quantile(row: pd.Series, quantile: float) -> float:
    for key in (quantile, str(quantile)):
        if key in row:
            return float(row[key])
    raise KeyError(f"Missing quantile column {quantile}")


def estimate_undersleep_probability(
    prediction: PredictionResult,
    snop_hours: float,
) -> float:
    """Estimate probability of sleeping less than SNOP.

    Uses a normal approximation from the 10th/50th/90th percentiles
    to estimate P(sleep < snop_hours).

    Args:
        prediction: PredictionResult from predict_sleep_duration
        snop_hours: User's Sleep Need for Optimal Performance

    Returns:
        Probability between 0.0 and 1.0
    """
    if snop_hours <= 0:
        return 0.0

    # Approximate std from the quantile spread
    # For normal distribution: P90 - P10 ≈ 2.563 * sigma
    spread = prediction.ci_high - prediction.ci_low
    if spread <= 0:
        # Point estimate — if predicted < snop, probability is high
        return 0.8 if prediction.predicted_hours < snop_hours else 0.1

    sigma = spread / 2.563
    if sigma <= 0:
        return 0.8 if prediction.predicted_hours < snop_hours else 0.1

    # Z-score for snop_hours threshold
    z = (snop_hours - prediction.predicted_hours) / sigma

    # Approximate CDF using error function
    from math import erf, sqrt

    probability = 0.5 * (1 + erf(z / sqrt(2)))
    return round(max(0.0, min(1.0, probability)), 3)
