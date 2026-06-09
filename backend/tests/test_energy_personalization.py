from datetime import datetime, timedelta, timezone
from types import SimpleNamespace

from app.services.energy_personalization import build_zone_adjustment_profile
from tests.conftest import TEST_USER_ID


def _user(chronotype: str = "Neutral") -> SimpleNamespace:
    return SimpleNamespace(
        user_id=TEST_USER_ID,
        email="user@example.com",
        chronotype=chronotype,
        snop_hours=8.0,
        created_at=datetime(2026, 5, 1, tzinfo=timezone.utc),
    )


def _night(day: int, *, start_hour: int = 23, duration_mins: int = 480) -> SimpleNamespace:
    start = datetime(2026, 5, day, start_hour, 0, tzinfo=timezone.utc)
    end = start + timedelta(minutes=duration_mins)
    return SimpleNamespace(
        user_id=TEST_USER_ID,
        start_time=start,
        end_time=end,
        duration_mins=duration_mins,
        session_type="nightly",
    )


def test_night_owl_shifts_circadian_zones_later():
    profile = build_zone_adjustment_profile(
        user=_user("Night Owl"),
        sessions=[_night(27), _night(28), _night(29)],
        target_date=datetime(2026, 5, 30, tzinfo=timezone.utc),
        sleep_debt_mins=0,
        exertion_score=0,
    )

    assert profile.circadian_offset_mins == 30


def test_early_bird_shifts_circadian_zones_earlier():
    profile = build_zone_adjustment_profile(
        user=_user("Early Bird"),
        sessions=[_night(27), _night(28), _night(29)],
        target_date=datetime(2026, 5, 30, tzinfo=timezone.utc),
        sleep_debt_mins=0,
        exertion_score=0,
    )

    assert profile.circadian_offset_mins == -30


def test_irregular_sleep_halves_chronotype_adjustment():
    profile = build_zone_adjustment_profile(
        user=_user("Night Owl"),
        sessions=[
            _night(27, start_hour=20),
            _night(28, start_hour=23),
            _night(29, start_hour=2),
        ],
        target_date=datetime(2026, 5, 30, tzinfo=timezone.utc),
        sleep_debt_mins=0,
        exertion_score=0,
    )

    assert profile.circadian_offset_mins == 15


def test_high_sleep_debt_boosts_recovery_priorities():
    profile = build_zone_adjustment_profile(
        user=_user(),
        sessions=[_night(27), _night(28), _night(29)],
        target_date=datetime(2026, 5, 30, tzinfo=timezone.utc),
        sleep_debt_mins=500,
        exertion_score=0,
    )

    assert profile.nudge_priority_adjustments["power_nap"] == 2
    assert profile.nudge_priority_adjustments["wind_down"] == 2
    assert profile.nudge_priority_adjustments["caffeine_cutoff"] == 2


def test_high_exertion_starts_sleep_window_earlier_and_boosts_wind_down():
    profile = build_zone_adjustment_profile(
        user=_user(),
        sessions=[_night(27), _night(28), _night(29)],
        target_date=datetime(2026, 5, 30, tzinfo=timezone.utc),
        sleep_debt_mins=0,
        exertion_score=0.8,
    )

    assert profile.sleep_window_offset_mins == -15
    assert profile.nudge_priority_adjustments["wind_down"] == 1


def test_short_previous_night_shortens_wake_window_and_extends_afternoon_dip():
    profile = build_zone_adjustment_profile(
        user=_user(),
        sessions=[_night(27), _night(28), _night(29, duration_mins=330)],
        target_date=datetime(2026, 5, 30, tzinfo=timezone.utc),
        sleep_debt_mins=0,
        exertion_score=0,
    )

    assert profile.afternoon_dip_offset_mins == -30
    assert profile.afternoon_dip_duration_delta_mins == 30
    assert profile.sleep_window_offset_mins == -30
    assert profile.wake_window_pressure_threshold == 0.50
