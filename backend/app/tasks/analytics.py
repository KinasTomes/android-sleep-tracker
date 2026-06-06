import asyncio
import uuid
from datetime import datetime, timezone

from app.cache.energy_cache import close_redis, invalidate_energy_cache
from app.db.session import async_session_factory, engine
from app.services.calibration import calibrate_snop
from app.services.energy_schedule import get_user_energy_schedule
from app.tasks.worker import celery_app



@celery_app.task(name="tasks.analytics.recalculate_energy", queue="analytics")
def recalculate_energy(user_id: str):
    """Trigger full energy schedule recalculation for a user.

    Called after new sleep data is ingested. Runs on dedicated
    analytics worker to avoid competing with storage inserts.
    """
    asyncio.run(_run_and_dispose(_recalculate_async(user_id)))


async def _run_and_dispose(coro):
    try:
        return await coro
    finally:
        await close_redis()
        await engine.dispose()


async def _recalculate_async(user_id: str):
    uid = uuid.UUID(user_id)
    async with async_session_factory() as db:
        await calibrate_snop(db, uid)
        
        #Xoá cache của ngày hôm nay trước
        today = datetime.now(timezone.utc).replace(hour=0, minute=0, second=0, microsecond=0)
        await invalidate_energy_cache(uid, today)
        
        await get_user_energy_schedule(db, uid)


@celery_app.task(name="tasks.analytics.batch_recalculate", queue="analytics")
def batch_recalculate(user_ids: list[str]):
    """Batch recalculation for morning sync burst.

    Triggered when many users wake up and sync simultaneously.
    """
    for uid in user_ids:
        recalculate_energy.delay(uid)


@celery_app.task(name="tasks.analytics.calibrate_snop", queue="analytics")
def calibrate_snop_task(user_id: str):
    """Standalone SNOP calibration task."""
    asyncio.run(_run_and_dispose(_calibrate_async(user_id)))


async def _calibrate_async(user_id: str):
    uid = uuid.UUID(user_id)
    async with async_session_factory() as db:
        await calibrate_snop(db, uid)
        await db.commit()
