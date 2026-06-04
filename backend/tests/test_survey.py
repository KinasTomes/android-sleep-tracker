import pytest

from app.api.v1 import survey as survey_api
from app.core.exceptions import ValidationError
from app.schemas.survey import SurveyAnswer, SurveySubmitRequest
from tests.conftest import FakeDb, TEST_USER_ID


@pytest.mark.asyncio
async def test_submit_survey_requires_seed_calibration_questions():
    payload = SurveySubmitRequest(
        answers=[
            SurveyAnswer(question_id="age", answer_key="30"),
            SurveyAnswer(question_id="gender", answer_key="M"),
        ]
    )

    with pytest.raises(ValidationError) as exc:
        await survey_api.submit_survey(payload=payload, user_id=TEST_USER_ID, db=FakeDb())

    assert exc.value.status_code == 422
    assert "sleep_schedule" in exc.value.detail


@pytest.mark.asyncio
async def test_submit_survey_upserts_answers_and_calibrates(monkeypatch):
    calls = []

    async def fake_calibrate_snop(db, user_id):
        calls.append(user_id)
        return 7.5

    monkeypatch.setattr(survey_api, "calibrate_snop", fake_calibrate_snop)
    payload = SurveySubmitRequest(
        answers=[
            SurveyAnswer(question_id="age", answer_key="30"),
            SurveyAnswer(question_id="gender", answer_key="M"),
            SurveyAnswer(question_id="sleep_schedule", answer_key="7-8h"),
        ]
    )
    db = FakeDb()

    responses = await survey_api.submit_survey(payload=payload, user_id=TEST_USER_ID, db=db)

    assert len(responses) == 3
    assert len(db.added) == 3
    assert db.flushed is True
    assert calls == [TEST_USER_ID]
