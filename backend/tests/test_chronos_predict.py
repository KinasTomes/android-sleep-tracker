from datetime import date

from app.algorithms import chronos_predict
from app.algorithms.chronos_predict import SleepHistoryPoint, predict_sleep_duration


class FakeChronosPipeline:
    def __init__(self):
        self.context_df = None
        self.kwargs = None

    def predict_df(self, context_df, **kwargs):
        self.context_df = context_df
        self.kwargs = kwargs
        return context_df.assign(**{"0.1": 6.0, "0.5": 7.0, "0.9": 8.0}).tail(1)


def test_predict_sleep_duration_passes_real_timestamps_to_chronos(monkeypatch):
    pipeline = FakeChronosPipeline()
    monkeypatch.setattr(chronos_predict, "_get_pipeline", lambda: pipeline)

    result = predict_sleep_duration([
        SleepHistoryPoint(date(2026, 6, 1), 6.5),
        SleepHistoryPoint(date(2026, 6, 2), 7.0),
        SleepHistoryPoint(date(2026, 6, 3), 7.5),
    ])

    assert result is not None
    assert result.predicted_hours == 7.0
    assert pipeline.kwargs["timestamp_column"] == "timestamp"
    assert list(pipeline.context_df["timestamp"].dt.date) == [
        date(2026, 6, 1),
        date(2026, 6, 2),
        date(2026, 6, 3),
    ]
    assert list(pipeline.context_df["target"]) == [6.5, 7.0, 7.5]


def test_predict_sleep_duration_regularizes_missing_sleep_dates(monkeypatch):
    pipeline = FakeChronosPipeline()
    monkeypatch.setattr(chronos_predict, "_get_pipeline", lambda: pipeline)

    result = predict_sleep_duration([
        SleepHistoryPoint(date(2026, 6, 1), 6.0),
        SleepHistoryPoint(date(2026, 6, 3), 8.0),
        SleepHistoryPoint(date(2026, 6, 5), 6.0),
    ])

    assert result is not None
    assert list(pipeline.context_df["timestamp"].dt.date) == [
        date(2026, 6, 1),
        date(2026, 6, 2),
        date(2026, 6, 3),
        date(2026, 6, 4),
        date(2026, 6, 5),
    ]
    assert list(pipeline.context_df["target"]) == [6.0, 7.0, 8.0, 7.0, 6.0]
