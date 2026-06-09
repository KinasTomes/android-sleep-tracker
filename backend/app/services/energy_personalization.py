from dataclasses import dataclass, field
from datetime import datetime, timedelta
from statistics import pstdev
from typing import Any


RECOVERY_NUDGE_TYPES = {"power_nap", "wind_down", "caffeine_cutoff"}


@dataclass(frozen=True)
class ZoneAdjustmentProfile:
    circadian_offset_mins: int = 0
    afternoon_dip_offset_mins: int = 0
    afternoon_dip_duration_delta_mins: int = 0
    sleep_window_offset_mins: int = 0
    wake_window_pressure_threshold: float = 0.60
    wind_down_duration_mins: int = 120
    nudge_priority_adjustments: dict[str, int] = field(default_factory=dict)


def build_zone_adjustment_profile(
    user: Any,
    sessions: list[Any],
    target_date: datetime,
    sleep_debt_mins: float,
    exertion_score: float,
) -> ZoneAdjustmentProfile:
    """Build deterministic v1 personalization adjustments for energy zones."""
    snop_mins = user.snop_hours * 60
    regularity_confidence = _sleep_regularity_confidence(sessions)
    circadian_offset = _chronotype_offset(user.chronotype, regularity_confidence)

    sleep_ratio = _previous_night_sleep_ratio(sessions, target_date, snop_mins)
    sleep_shortfall = _sleep_shortfall_adjustments(sleep_ratio)

    exertion_sleep_offset = -15 if exertion_score > 0.7 else 0
    sleep_window_offset = _clamp_offset(
        sleep_shortfall["sleep_window_offset_mins"] + exertion_sleep_offset
    )

    priority_adjustments = _sleep_debt_priority_adjustments(sleep_debt_mins, snop_mins)
    if exertion_score > 0.7:
        priority_adjustments["wind_down"] = priority_adjustments.get("wind_down", 0) + 1

    return ZoneAdjustmentProfile(
        circadian_offset_mins=circadian_offset,
        afternoon_dip_offset_mins=sleep_shortfall["afternoon_dip_offset_mins"],
        afternoon_dip_duration_delta_mins=sleep_shortfall["afternoon_dip_duration_delta_mins"],
        sleep_window_offset_mins=sleep_window_offset,
        wake_window_pressure_threshold=sleep_shortfall["wake_window_pressure_threshold"],
        wind_down_duration_mins=120,
        nudge_priority_adjustments=priority_adjustments,
    )


def _chronotype_offset(chronotype: str, regularity_confidence: float) -> int:
    base_offsets = {
        "Early Bird": -30,
        "Neutral": 0,
        "Night Owl": 30,
    }
    base = base_offsets.get(chronotype, 0)
    return _clamp_offset(round(base * regularity_confidence))


def _sleep_regularity_confidence(sessions: list[Any]) -> float:
    nightly = [s for s in sessions if s.session_type == "nightly"][-7:]
    if len(nightly) < 3:
        return 0.0

    bedtimes = [_minutes_since_midnight(s.start_time, bedtime=True) for s in nightly]
    wake_times = [_minutes_since_midnight(s.end_time) for s in nightly]
    if max(pstdev(bedtimes), pstdev(wake_times)) > 90:
        return 0.5
    return 1.0


def _minutes_since_midnight(value: datetime, *, bedtime: bool = False) -> int:
    minute = value.hour * 60 + value.minute
    if bedtime and minute < 12 * 60:
        minute += 24 * 60
    return minute


def _previous_night_sleep_ratio(
    sessions: list[Any],
    target_date: datetime,
    snop_mins: float,
) -> float | None:
    if snop_mins <= 0:
        return None

    target_day = target_date.date()
    candidates = [
        s
        for s in sessions
        if s.session_type == "nightly"
        and s.end_time.date() == target_day
        and s.end_time <= target_date + timedelta(days=1)
    ]
    if not candidates:
        return None

    latest = max(candidates, key=lambda s: s.end_time)
    return latest.duration_mins / snop_mins


def _sleep_shortfall_adjustments(sleep_ratio: float | None) -> dict[str, int | float]:
    if sleep_ratio is None or sleep_ratio >= 0.95:
        return {
            "afternoon_dip_offset_mins": 0,
            "afternoon_dip_duration_delta_mins": 0,
            "sleep_window_offset_mins": 0,
            "wake_window_pressure_threshold": 0.60,
        }
    if sleep_ratio < 0.75:
        return {
            "afternoon_dip_offset_mins": -30,
            "afternoon_dip_duration_delta_mins": 30,
            "sleep_window_offset_mins": -30,
            "wake_window_pressure_threshold": 0.50,
        }
    return {
        "afternoon_dip_offset_mins": -15,
        "afternoon_dip_duration_delta_mins": 15,
        "sleep_window_offset_mins": -15,
        "wake_window_pressure_threshold": 0.55,
    }


def _sleep_debt_priority_adjustments(
    sleep_debt_mins: float,
    snop_mins: float,
) -> dict[str, int]:
    if snop_mins <= 0:
        return {}

    debt_ratio = sleep_debt_mins / snop_mins
    if debt_ratio > 1.0:
        boost = 2
    elif debt_ratio > 0.5:
        boost = 1
    else:
        return {}

    return {nudge_type: boost for nudge_type in RECOVERY_NUDGE_TYPES}


def _clamp_offset(value: int) -> int:
    return max(-60, min(60, value))
