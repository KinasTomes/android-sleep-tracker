from datetime import datetime, timezone
from types import SimpleNamespace

import numpy as np
import pytest

from app.schemas.energy import EnergyScheduleResponse

pytest.importorskip("asyncpg")
pytest.importorskip("scipy")

from app.services import energy_schedule
from tests.conftest import FakeDb, FakeResult, TEST_USER_ID


@pytest.mark.asyncio
async def test_cached_energy_response_is_marked_cached(monkeypatch):
    cached = EnergyScheduleResponse(
        user_id=TEST_USER_ID,
        date=datetime(2026, 6, 1, tzinfo=timezone.utc),
        energy_points=[],
        zones=[],
        nudges=[],
        energy_potential=50.0,
        is_cached=False,
    )

    async def fake_get_cached_energy(user_id, date):
        return cached

    monkeypatch.setattr(energy_schedule, "get_cached_energy", fake_get_cached_energy)

    response = await energy_schedule.get_user_energy_schedule(
        db=FakeDb(),
        user_id=TEST_USER_ID,
        date=datetime(2026, 6, 1, tzinfo=timezone.utc),
    )

    assert response.is_cached is True


@pytest.mark.asyncio
async def test_energy_schedule_passes_personalization_profile(monkeypatch, user):
    from app.models.sleep_session import SleepSession

    date = datetime(2026, 5, 30, tzinfo=timezone.utc)
    sessions = [
        SleepSession(
            user_id=TEST_USER_ID,
            start_time=datetime(2026, 5, 29, 23, 0, tzinfo=timezone.utc),
            end_time=datetime(2026, 5, 30, 7, 0, tzinfo=timezone.utc),
            duration_mins=480,
            session_type="nightly",
        )
    ]
    profile = object()
    captured = {}

    class SequenceDb(FakeDb):
        def __init__(self):
            super().__init__(users={TEST_USER_ID: user})
            self.results = [sessions, []]

        async def execute(self, statement):
            self.executed_statements.append(statement)
            return FakeResult(values=self.results.pop(0))

    async def fake_get_cached_energy(user_id, requested_date):
        return None

    async def fake_set_cached_energy(user_id, requested_date, response):
        captured["cached_response"] = response

    async def fake_compute_exertion_score(db, user_id):
        return 0.8

    def fake_build_profile(**kwargs):
        captured["profile_kwargs"] = kwargs
        return profile

    def fake_compute_energy_schedule(*args, **kwargs):
        captured["schedule_kwargs"] = kwargs
        return SimpleNamespace(
            energy_values=np.array([50.0]),
            zones=[],
            nudges=[],
            energy_potential_score=80.0,
        )

    monkeypatch.setattr(energy_schedule, "get_cached_energy", fake_get_cached_energy)
    monkeypatch.setattr(energy_schedule, "set_cached_energy", fake_set_cached_energy)
    monkeypatch.setattr(energy_schedule, "compute_exertion_score", fake_compute_exertion_score)
    monkeypatch.setattr(energy_schedule, "build_zone_adjustment_profile", fake_build_profile)
    monkeypatch.setattr(energy_schedule, "compute_energy_schedule", fake_compute_energy_schedule)

    response = await energy_schedule.get_user_energy_schedule(
        db=SequenceDb(),
        user_id=TEST_USER_ID,
        date=date,
    )

    assert captured["profile_kwargs"]["user"] is user
    assert captured["profile_kwargs"]["sessions"] == sessions
    assert captured["profile_kwargs"]["target_date"] == date
    assert captured["profile_kwargs"]["exertion_score"] == 0.8
    assert captured["schedule_kwargs"]["adjustment_profile"] is profile
    assert type(response).model_fields.keys() == EnergyScheduleResponse.model_fields.keys()
    assert captured["cached_response"] is response
