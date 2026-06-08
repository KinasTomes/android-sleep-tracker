from datetime import datetime, timezone

import numpy as np
import pytest

from app.algorithms.sleep_debt import compute_sleep_debt, format_sleep_debt


def _compute_energy_schedule_if_available():
    pytest.importorskip("scipy")
    from app.algorithms.synthesis import compute_energy_schedule

    return compute_energy_schedule


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
