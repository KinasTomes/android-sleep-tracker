from datetime import datetime, timezone

import pytest

from app.schemas.energy import EnergyScheduleResponse
from app.services import energy_schedule
from tests.conftest import FakeDb, TEST_USER_ID


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
