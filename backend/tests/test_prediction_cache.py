import uuid
from datetime import datetime, timezone

import pytest

from app.cache import prediction_cache


class FakeRedis:
    def __init__(self, value):
        self.value = value

    async def get(self, key):
        return self.value


async def _fake_get_redis(value):
    return FakeRedis(value)


@pytest.mark.asyncio
async def test_get_cached_prediction_marks_response_as_cached(monkeypatch):
    user_id = uuid.UUID("00000000-0000-0000-0000-000000000001")
    cached_payload = """
    {
        "user_id": "00000000-0000-0000-0000-000000000001",
        "predicted_duration_hours": 7.0,
        "confidence_interval_low": 6.0,
        "confidence_interval_high": 8.0,
        "probability_undersleep": 0.4,
        "snop_hours": 8.0,
        "recommendation": "cached",
        "prediction_date": "2026-06-06T00:00:00Z",
        "based_on_nights": 14,
        "is_cached": false
    }
    """
    monkeypatch.setattr(prediction_cache, "get_redis", lambda: _fake_get_redis(cached_payload))

    response = await prediction_cache.get_cached_prediction(
        user_id,
        datetime(2026, 6, 6, tzinfo=timezone.utc),
    )

    assert response is not None
    assert response.is_cached is True
