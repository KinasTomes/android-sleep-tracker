"""Redis cache for sleep predictions.

Follows the same pattern as energy_cache.py.
"""

import json
import uuid
from datetime import datetime

from app.cache.energy_cache import get_redis
from app.core.config import settings
from app.schemas.prediction import SleepPredictionResponse


def _cache_key(user_id: uuid.UUID, date: datetime) -> str:
    return f"prediction:sleep:{user_id}:{date.strftime('%Y-%m-%d')}"


async def get_cached_prediction(
    user_id: uuid.UUID, date: datetime
) -> SleepPredictionResponse | None:
    r = await get_redis()
    data = await r.get(_cache_key(user_id, date))
    if data:
        parsed = json.loads(data)
        parsed["is_cached"] = True
        return SleepPredictionResponse.model_validate(parsed)
    return None


async def set_cached_prediction(
    user_id: uuid.UUID, date: datetime, response: SleepPredictionResponse
) -> None:
    r = await get_redis()
    payload = response.model_dump_json()
    await r.set(_cache_key(user_id, date), payload, ex=settings.CHRONOS_PREDICTION_CACHE_TTL)


async def invalidate_prediction_cache(user_id: uuid.UUID, date: datetime) -> None:
    r = await get_redis()
    await r.delete(_cache_key(user_id, date))
