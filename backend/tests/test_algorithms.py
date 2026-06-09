from datetime import datetime, timezone

import numpy as np
import pytest

from app.algorithms.sleep_debt import compute_sleep_debt, format_sleep_debt


def _compute_energy_schedule_if_available():
    pytest.importorskip("scipy")
    from app.algorithms.synthesis import compute_energy_schedule

    return compute_energy_schedule


def _zone_adjustment_profile_if_available():
    from app.services.energy_personalization import ZoneAdjustmentProfile

    return ZoneAdjustmentProfile


def test_compute_sleep_debt_sums_14_day_window_and_floors_at_zero():
    actual_sleep = np.array([420] * 13 + [480])

    debt = compute_sleep_debt(snop_mins=480, actual_sleep_mins=actual_sleep)

    assert debt == 780.0
    assert format_sleep_debt(debt) == "13h 0m"


def test_compute_sleep_debt_ignores_days_before_window():
    actual_sleep = np.array([0, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480])

    debt = compute_sleep_debt(snop_mins=480, actual_sleep_mins=actual_sleep)

    assert debt == 0.0


def test_energy_schedule_includes_process_s_wake_window():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(wake_time, h_at_wake=0.0, phi=0.0)

    wake_window = next(z for z in schedule.zones if z.zone_type == "wake_window")
    assert wake_window.start_minute == 0
    assert 960 <= wake_window.end_minute <= 1050


def test_sleep_debt_shortens_wake_window():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    rested = compute_energy_schedule(wake_time, h_at_wake=0.0, phi=0.0)
    indebted = compute_energy_schedule(wake_time, h_at_wake=0.4, phi=0.0)

    rested_window = next(z for z in rested.zones if z.zone_type == "wake_window")
    indebted_window = next(z for z in indebted.zones if z.zone_type == "wake_window")
    assert indebted_window.end_minute < rested_window.end_minute


def test_wind_down_is_separate_zone_before_melatonin_window():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(wake_time, h_at_wake=0.0, phi=0.0)

    wind_down = next(z for z in schedule.zones if z.zone_type == "wind_down")
    melatonin = next(z for z in schedule.zones if z.zone_type == "melatonin_window")
    assert wind_down.end_minute == melatonin.start_minute
    assert (wind_down.end_minute - wind_down.start_minute) % 1440 == 120


def test_wind_down_nudge_starts_at_wind_down_zone_start():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(wake_time, h_at_wake=0.0, phi=0.0)

    wind_down = next(z for z in schedule.zones if z.zone_type == "wind_down")
    wind_down_nudge = next(n for n in schedule.nudges if n.nudge_type == "wind_down")
    assert wind_down_nudge.minute == wind_down.start_minute


def test_personalization_shifts_circadian_zones_and_keeps_schema_shape():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    ZoneAdjustmentProfile = _zone_adjustment_profile_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    base = compute_energy_schedule(wake_time, h_at_wake=0.0, phi=0.0)
    shifted = compute_energy_schedule(
        wake_time,
        h_at_wake=0.0,
        phi=0.0,
        adjustment_profile=ZoneAdjustmentProfile(circadian_offset_mins=30),
    )

    base_morning = next(z for z in base.zones if z.zone_type == "morning_peak")
    shifted_morning = next(z for z in shifted.zones if z.zone_type == "morning_peak")
    assert shifted_morning.start_minute == base_morning.start_minute + 30
    assert len(shifted.zones) == len(base.zones)


def test_morning_peak_starts_after_grogginess_even_with_early_offset():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    ZoneAdjustmentProfile = _zone_adjustment_profile_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(
        wake_time,
        h_at_wake=0.0,
        phi=0.0,
        adjustment_profile=ZoneAdjustmentProfile(circadian_offset_mins=-60),
    )

    morning_peak = next(z for z in schedule.zones if z.zone_type == "morning_peak")
    assert morning_peak.start_minute >= 90


def test_display_zones_do_not_overlap():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    ZoneAdjustmentProfile = _zone_adjustment_profile_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(
        wake_time,
        h_at_wake=0.0,
        phi=0.0,
        adjustment_profile=ZoneAdjustmentProfile(
            circadian_offset_mins=-60,
            afternoon_dip_offset_mins=-30,
            afternoon_dip_duration_delta_mins=30,
            sleep_window_offset_mins=-60,
        ),
    )

    display_zones = sorted(
        [z for z in schedule.zones if z.zone_type != "wake_window"],
        key=lambda z: (z.start_minute, z.end_minute),
    )
    for previous, current in zip(display_zones, display_zones[1:]):
        assert previous.end_minute <= current.start_minute


def test_personalization_boosts_nudge_priority():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    ZoneAdjustmentProfile = _zone_adjustment_profile_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(
        wake_time,
        h_at_wake=0.0,
        phi=0.0,
        adjustment_profile=ZoneAdjustmentProfile(
            nudge_priority_adjustments={"wind_down": 2}
        ),
    )

    wind_down = next(n for n in schedule.nudges if n.nudge_type == "wind_down")
    assert wind_down.priority == 5


def test_disabled_habit_preferences_filter_after_priority_adjustment():
    compute_energy_schedule = _compute_energy_schedule_if_available()
    ZoneAdjustmentProfile = _zone_adjustment_profile_if_available()
    wake_time = datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc)

    schedule = compute_energy_schedule(
        wake_time,
        h_at_wake=0.0,
        phi=0.0,
        enabled_habit_ids={"light_exposure"},
        adjustment_profile=ZoneAdjustmentProfile(
            nudge_priority_adjustments={"wind_down": 2}
        ),
    )

    assert [n.nudge_type for n in schedule.nudges] == ["light_exposure"]
