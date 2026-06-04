import numpy as np

from app.algorithms.sleep_debt import compute_sleep_debt, format_sleep_debt


def test_compute_sleep_debt_sums_14_day_window_and_floors_at_zero():
    actual_sleep = np.array([420] * 13 + [480])

    debt = compute_sleep_debt(snop_mins=480, actual_sleep_mins=actual_sleep)

    assert debt == 780.0
    assert format_sleep_debt(debt) == "13h 0m"


def test_compute_sleep_debt_ignores_days_before_window():
    actual_sleep = np.array([0, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480])

    debt = compute_sleep_debt(snop_mins=480, actual_sleep_mins=actual_sleep)

    assert debt == 0.0
