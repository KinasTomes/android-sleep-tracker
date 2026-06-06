from datetime import datetime, timezone

from app.models.sleep_session import SleepSession
from app.services.prediction_service import (
    _aggregate_daily_sleep_hours,
    _aggregate_sleep_history,
    _session_sleep_date,
)
from tests.conftest import TEST_USER_ID


def _session(start_time: datetime, duration_mins: int, session_type: str) -> SleepSession:
    return SleepSession(
        user_id=TEST_USER_ID,
        start_time=start_time,
        end_time=start_time,
        duration_mins=duration_mins,
        session_type=session_type,
    )


def test_nightly_session_after_midnight_belongs_to_previous_sleep_date():
    session = _session(
        datetime(2026, 6, 5, 1, 30, tzinfo=timezone.utc),
        duration_mins=420,
        session_type="nightly",
    )

    assert _session_sleep_date(session).isoformat() == "2026-06-04"


def test_nap_session_stays_on_start_calendar_date():
    session = _session(
        datetime(2026, 6, 5, 1, 30, tzinfo=timezone.utc),
        duration_mins=60,
        session_type="nap",
    )

    assert _session_sleep_date(session).isoformat() == "2026-06-05"


def test_aggregate_sleep_hours_keeps_latest_14_sleep_dates_chronologically():
    sessions = [
        _session(
            datetime(2026, 6, day, 23, 0, tzinfo=timezone.utc),
            duration_mins=day * 60,
            session_type="nightly",
        )
        for day in range(1, 16)
    ]

    assert _aggregate_daily_sleep_hours(sessions) == [float(day) for day in range(2, 16)]


def test_aggregate_sleep_history_keeps_sleep_dates_with_hours():
    sessions = [
        _session(
            datetime(2026, 6, 4, 23, 0, tzinfo=timezone.utc),
            duration_mins=360,
            session_type="nightly",
        ),
        _session(
            datetime(2026, 6, 5, 14, 0, tzinfo=timezone.utc),
            duration_mins=60,
            session_type="nap",
        ),
    ]

    assert [
        (point.sleep_date.isoformat(), point.hours)
        for point in _aggregate_sleep_history(sessions)
    ] == [("2026-06-04", 6.0), ("2026-06-05", 1.0)]


def test_aggregate_sleep_hours_combines_night_after_midnight_and_same_day_nap():
    sessions = [
        _session(
            datetime(2026, 6, 5, 1, 0, tzinfo=timezone.utc),
            duration_mins=360,
            session_type="nightly",
        ),
        _session(
            datetime(2026, 6, 4, 14, 0, tzinfo=timezone.utc),
            duration_mins=90,
            session_type="nap",
        ),
    ]

    assert _aggregate_daily_sleep_hours(sessions) == [7.5]
