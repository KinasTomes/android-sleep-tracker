from celery import Celery
from celery.schedules import crontab

from app.core.config import settings

celery_app = Celery(
    "lumi_worker",
    broker=settings.CELERY_BROKER_URL,
    backend=settings.CELERY_RESULT_BACKEND,
)

celery_app.conf.update(
    task_serializer="json",
    accept_content=["json"],
    result_serializer="json",
    timezone="UTC",
    enable_utc=True,
    task_track_started=True,
    task_acks_late=True,
    worker_prefetch_multiplier=1,

    beat_schedule={
        "dispatch-daily-nudges-at-midnight": {
            "task": "tasks.notifications.dispatch_all_users_nudges",
            #"schedule": crontab(hour=0, minute=0),  # Chạy vào 00:00 UTC mỗi ngày
            "schedule": crontab(minute='*'),  # CHÚ Ý: Chạy mỗi phút 1 lần
        }
    }
)

celery_app.autodiscover_tasks(["app.tasks.storage", "app.tasks.analytics", "app.tasks.notifications"])
