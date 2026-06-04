import uuid
from dataclasses import dataclass
from datetime import datetime, timezone

import pytest


TEST_USER_ID = uuid.UUID("00000000-0000-0000-0000-000000000001")


@dataclass
class FakeScalarResult:
    values: list

    def all(self):
        return self.values


class FakeResult:
    def __init__(self, values=None, rows=None):
        self.values = values or []
        self.rows = rows or []

    def scalar_one_or_none(self):
        return self.values[0] if self.values else None

    def scalars(self):
        return FakeScalarResult(self.values)

    def all(self):
        return self.rows

    def first(self):
        return self.rows[0] if self.rows else None

    def one(self):
        if self.rows:
            return self.rows[0]
        raise AssertionError("FakeResult.one() called with no rows")


class FakeDb:
    def __init__(self, *, users=None, execute_values=None):
        self.users = users or {}
        self.execute_values = execute_values or []
        self.added = []
        self.deleted = []
        self.flushed = False
        self.committed = False
        self.executed_statements = []

    async def get(self, model, key):
        if model.__name__ == "User":
            return self.users.get(key)
        return None

    async def execute(self, statement):
        self.executed_statements.append(statement)
        return FakeResult(values=list(self.execute_values))

    def add(self, value):
        self.added.append(value)

    async def delete(self, value):
        self.deleted.append(value)

    async def flush(self):
        self.flushed = True

    async def commit(self):
        self.committed = True


@pytest.fixture
def user():
    from app.models.user import User

    return User(
        user_id=TEST_USER_ID,
        email="user@example.com",
        chronotype="Neutral",
        snop_hours=8.0,
        created_at=datetime.now(timezone.utc),
    )
