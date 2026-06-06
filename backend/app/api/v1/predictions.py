import uuid

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user_id
from app.db.session import get_db
from app.schemas.prediction import SleepPredictionResponse
from app.services.prediction_service import get_sleep_prediction

router = APIRouter(prefix="/predictions", tags=["predictions"])


@router.get("/sleep", response_model=SleepPredictionResponse)
async def predict_sleep(
    user_id: uuid.UUID = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db),
):
    """Predict tonight's sleep duration using Chronos-2.

    Returns probabilistic prediction with confidence intervals
    based on recent sleep history. Requires at least 3 nights
    of sleep data for meaningful predictions.

    Results are cached for 12 hours.
    """
    return await get_sleep_prediction(db, user_id)
