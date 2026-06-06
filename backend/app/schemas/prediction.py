import uuid
from datetime import datetime

from pydantic import BaseModel


class SleepPredictionResponse(BaseModel):
    user_id: uuid.UUID
    predicted_duration_hours: float  # median (50th percentile)
    confidence_interval_low: float  # 10th percentile
    confidence_interval_high: float  # 90th percentile
    probability_undersleep: float  # P(duration < snop_hours)
    snop_hours: float  # user's current SNOP
    recommendation: str  # human-readable advice
    prediction_date: datetime  # đêm được dự đoán
    based_on_nights: int  # số đêm data dùng để predict
    is_cached: bool = False
