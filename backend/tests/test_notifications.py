from app.tasks.worker import celery_app


def test_daily_nudge_dispatch_has_periodic_schedule_registered():
    scheduled_tasks = {
        entry.get("task")
        for entry in celery_app.conf.beat_schedule.values()
    }

    assert "tasks.notifications.dispatch_all_users_nudges" in scheduled_tasks
