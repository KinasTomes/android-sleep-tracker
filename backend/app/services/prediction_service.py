"""Sleep prediction service.

Orchestrates: query sleep history → Chronos-2 inference → cache → response.
"""

import uuid
from datetime import date, datetime, timedelta, timezone

from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.algorithms.chronos_predict import (
    SleepHistoryPoint,
    estimate_undersleep_probability,
    predict_sleep_duration,
)
from app.cache.prediction_cache import get_cached_prediction, set_cached_prediction
from app.core.config import settings
from app.models.sleep_session import SleepSession
from app.models.user import User
from app.schemas.prediction import SleepPredictionResponse


async def get_sleep_prediction(
    db: AsyncSession,
    user_id: uuid.UUID,
) -> SleepPredictionResponse:
    """Get sleep duration prediction for tonight.

    Pipeline:
    1. Check Redis cache
    2. Query sleep history (last N nights)
    3. If insufficient data, return fallback response
    4. Run Chronos-2 zero-shot prediction
    5. Compute undersleep probability
    6. Generate recommendation
    7. Cache and return
    """
    now = datetime.now(timezone.utc)
    prediction_date = now.replace(hour=0, minute=0, second=0, microsecond=0)

    # 1. Check cache
    cached = await get_cached_prediction(user_id, prediction_date)
    if cached:
        return cached

    # 2. Fetch user
    user = await db.get(User, user_id)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"User {user_id} not found",
        )

    # 3. Query sleep history from the window boundary at midnight so the
    # current time of day does not cut off part of the oldest sleep date.
    window_start = prediction_date - timedelta(days=settings.CHRONOS_HISTORY_WINDOW)
    stmt = (
        select(SleepSession)
        .where(SleepSession.user_id == user_id)
        .where(SleepSession.start_time >= window_start)
        .order_by(SleepSession.start_time)
    )
    result = await db.execute(stmt)
    sessions = result.scalars().all()

    # 4. Aggregate daily sleep (combine nightly + nap per sleep date)
    sleep_history = _aggregate_sleep_history(sessions)
    daily_hours = [point.hours for point in sleep_history]

    # 5. Check minimum data requirement
    if len(sleep_history) < settings.CHRONOS_MIN_HISTORY_NIGHTS:
        response = _build_insufficient_data_response(
            user_id, prediction_date, user.snop_hours, len(sleep_history)
        )
        await set_cached_prediction(user_id, prediction_date, response)
        return response

    # 6. Run Chronos-2 prediction
    prediction = predict_sleep_duration(sleep_history)

    if prediction is None:
        # Model unavailable — fall back to simple average
        prediction = _fallback_prediction(daily_hours)

    # 7. Compute undersleep probability
    undersleep_prob = estimate_undersleep_probability(prediction, user.snop_hours)

    # 8. Generate recommendation
    recommendation = _generate_recommendation(
        prediction.predicted_hours, user.snop_hours, undersleep_prob
    )

    # 9. Build response
    response = SleepPredictionResponse(
        user_id=user_id,
        predicted_duration_hours=prediction.predicted_hours,
        confidence_interval_low=prediction.ci_low,
        confidence_interval_high=prediction.ci_high,
        probability_undersleep=undersleep_prob,
        snop_hours=user.snop_hours,
        recommendation=recommendation,
        prediction_date=prediction_date,
        based_on_nights=len(sleep_history),
        is_cached=False,
    )

    # 10. Cache
    await set_cached_prediction(user_id, prediction_date, response)

    return response


def _aggregate_sleep_history(sessions: list[SleepSession]) -> list[SleepHistoryPoint]:
    """Aggregate total sleep hours by sleep date from sessions.

    Nightly sessions that start after midnight are assigned to the previous
    sleep date. Nap sessions stay on their calendar date. Returns at most the
    configured history window, ordered chronologically for the model.
    """
    daily: dict[date, float] = {}
    for s in sessions:
        sleep_date = _session_sleep_date(s)
        daily[sleep_date] = daily.get(sleep_date, 0.0) + s.duration_mins / 60.0

    latest_nights = sorted(daily.items(), reverse=True)[: settings.CHRONOS_HISTORY_WINDOW]
    return [
        SleepHistoryPoint(sleep_date=sleep_date, hours=hours)
        for sleep_date, hours in sorted(latest_nights)
    ]


def _aggregate_daily_sleep_hours(sessions: list[SleepSession]) -> list[float]:
    """Return aggregated sleep hours for callers that only need durations."""
    return [point.hours for point in _aggregate_sleep_history(sessions)]


def _session_sleep_date(session: SleepSession) -> date:
    """Return the date bucket used for nightly sleep prediction history."""
    start_time = session.start_time
    if start_time.tzinfo is None:
        start_time = start_time.replace(tzinfo=timezone.utc)
    else:
        start_time = start_time.astimezone(timezone.utc)

    if session.session_type == "nightly":
        return (start_time - timedelta(hours=12)).date()
    return start_time.date()


def _fallback_prediction(sleep_history: list[float]):
    """Simple fallback when Chronos-2 is unavailable.

    Uses recent average with conservative confidence interval.
    """
    from app.algorithms.chronos_predict import PredictionResult

    import numpy as np

    arr = np.array(sleep_history)
    mean = float(np.mean(arr))
    std = float(np.std(arr)) if len(arr) > 1 else 1.0

    return PredictionResult(
        predicted_hours=round(mean, 2),
        ci_low=round(max(0, mean - 1.28 * std), 2),  # ~10th percentile
        ci_high=round(min(24, mean + 1.28 * std), 2),  # ~90th percentile
    )


def _build_insufficient_data_response(
    user_id: uuid.UUID,
    prediction_date: datetime,
    snop_hours: float,
    nights_available: int,
) -> SleepPredictionResponse:
    """Build response when not enough sleep data is available."""
    return SleepPredictionResponse(
        user_id=user_id,
        predicted_duration_hours=snop_hours,  # default to SNOP
        confidence_interval_low=max(0, snop_hours - 2),
        confidence_interval_high=min(24, snop_hours + 2),
        probability_undersleep=0.0,
        snop_hours=snop_hours,
        recommendation=(
            f"Chưa đủ dữ liệu giấc ngủ để dự đoán. "
            f"Cần ít nhất {settings.CHRONOS_MIN_HISTORY_NIGHTS} đêm, "
            f"hiện có {nights_available} đêm. "
            f"Hãy tiếp tục ghi nhận giấc ngủ để nhận dự đoán cá nhân hóa."
        ),
        prediction_date=prediction_date,
        based_on_nights=nights_available,
        is_cached=False,
    )


def _generate_recommendation(
    predicted_hours: float, snop_hours: float, undersleep_prob: float
) -> str:
    """Generate human-readable sleep recommendation."""
    diff = predicted_hours - snop_hours

    if undersleep_prob >= 0.7:
        return (
            f"Dự đoán bạn sẽ ngủ {predicted_hours:.1f} giờ — thấp hơn SNOP {snop_hours:.1f} giờ. "
            f"Xác suất thiếu ngủ: {undersleep_prob:.0%}. "
            f"Nên đi ngủ sớm hơn bình thường để bù đắp giấc ngủ."
        )
    elif undersleep_prob >= 0.4:
        return (
            f"Dự đoán bạn sẽ ngủ {predicted_hours:.1f} giờ, gần bằng SNOP {snop_hours:.1f} giờ. "
            f"Xác suất thiếu ngủ: {undersleep_prob:.0%}. "
            f"Hãy duy trì thói quen ngủ đều đặn."
        )
    elif diff >= 1.0:
        return (
            f"Dự đoán bạn sẽ ngủ {predicted_hours:.1f} giờ — đủ cho SNOP {snop_hours:.1f} giờ. "
            f"Chất lượng giấc ngủ đang tốt, hãy duy trì!"
        )
    else:
        return (
            f"Dự đoán bạn sẽ ngủ {predicted_hours:.1f} giờ, đạt SNOP {snop_hours:.1f} giờ. "
            f"Xác suất thiếu ngủ: {undersleep_prob:.0%}."
        )
