from datetime import datetime, timedelta, timezone

import pytest

from app.api.v1 import sleep as sleep_api
from app.models.sleep_session import SleepSession
from tests.conftest import FakeDb, FakeResult, TEST_USER_ID


class LimitingSleepDb(FakeDb):
    def __init__(self, *, user, sessions):
        super().__init__(users={TEST_USER_ID: user})
        self.sessions = sessions

    async def execute(self, statement):
        self.executed_statements.append(statement)
        limit_clause = getattr(statement, "_limit_clause", None)
        if limit_clause is not None:
            limit = int(limit_clause.value)
            return FakeResult(values=self.sessions[:limit])
        return FakeResult(values=self.sessions)


@pytest.mark.asyncio
async def test_get_sleep_debt_uses_all_sessions_in_14_day_window(user):
    now = datetime.now(timezone.utc).replace(hour=12, minute=0, second=0, microsecond=0)
    sessions = [
        SleepSession(
            user_id=TEST_USER_ID,
            start_time=now - timedelta(days=1, hours=i),
            end_time=now - timedelta(days=1, hours=i) + timedelta(minutes=60),
            duration_mins=60,
            session_type="nap",
        )
        for i in range(15)
    ]
    db = LimitingSleepDb(user=user, sessions=sessions)

    response = await sleep_api.get_sleep_debt(user_id=TEST_USER_ID, db=db)

    # Expected actual sleep is 15h within the 14-day window. If the endpoint
    # limits by 14 sessions instead of date range, this will be 1h too high.
    expected_debt = (14 * 8 * 60) - (15 * 60)
    assert response.sleep_debt_mins == expected_debt


@pytest.mark.asyncio
async def test_manual_sleep_input_rejects_short_sessions():
    from app.schemas.sleep import SleepSessionCreate

    payload = SleepSessionCreate(
        start_time=datetime(2026, 6, 1, 13, 0, tzinfo=timezone.utc),
        end_time=datetime(2026, 6, 1, 13, 3, tzinfo=timezone.utc),
        duration_mins=3,
    )

    response = await sleep_api.manual_sleep_input(payload=payload, user_id=TEST_USER_ID)

    assert response == {"error": "Session too short (minimum 5 minutes)", "sessions_dispatched": 0}
