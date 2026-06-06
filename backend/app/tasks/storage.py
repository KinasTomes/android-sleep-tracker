import asyncio
import uuid
from datetime import datetime, timezone

from app.cache.energy_cache import close_redis
from app.cache.prediction_cache import invalidate_prediction_cache
from app.db.session import async_session_factory, engine
from app.models.biometric import BiometricData
from app.models.sleep_session import SleepSession
from app.tasks.worker import celery_app


@celery_app.task(name="tasks.storage.insert_sleep_sessions", queue="storage")
def insert_sleep_sessions(user_id: str, sessions: list[dict]):
    """Batch INSERT sleep sessions into TimescaleDB hypertable.

    Runs as a dedicated storage worker to avoid blocking the API.
    After successful insert, chains recalculate_energy on the analytics queue.
    """
    asyncio.run(_run_and_dispose(_insert_sessions_async(user_id, sessions)))


async def _run_and_dispose(coro):
    try:
        return await coro
    finally:
        await close_redis()
        await engine.dispose()


async def _insert_sessions_async(user_id: str, sessions: list[dict]):
    from app.tasks.analytics import recalculate_energy

    parsed_user_id = uuid.UUID(user_id)
    async with async_session_factory() as db:
        for s in sessions:
            db.add(SleepSession(
                user_id=parsed_user_id,
                start_time=datetime.fromisoformat(s["start_time"]),
                end_time=datetime.fromisoformat(s["end_time"]),
                duration_mins=s["duration_mins"],
                session_type=s["session_type"],
            ))
        await db.commit()

    prediction_date = datetime.now(timezone.utc).replace(hour=0, minute=0, second=0, microsecond=0)
    await invalidate_prediction_cache(parsed_user_id, prediction_date)

    # Chain analytics: recalculate energy after storage completes
    recalculate_energy.delay(user_id)


@celery_app.task(name="tasks.storage.insert_biometric_data", queue="storage")
def insert_biometric_data(records: list[dict]):
    """Batch INSERT biometric data into TimescaleDB hypertable.

    Uses chunked inserts for large payloads from wearable syncs.
    """
    asyncio.run(_run_and_dispose(_insert_biometrics_async(records)))


async def _insert_biometrics_async(records: list[dict]):
    CHUNK_SIZE = 1000
    async with async_session_factory() as db:
        for i in range(0, len(records), CHUNK_SIZE):
            chunk = records[i : i + CHUNK_SIZE]
            for r in chunk:
                db.add(BiometricData(
                    user_id=r["user_id"],
                    time=datetime.fromisoformat(r["time"]),
                    metric_type=r["metric_type"],
                    value=r["value"],
                ))
            await db.flush()
        await db.commit()
