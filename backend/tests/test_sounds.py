import inspect

import pytest

from app.api.v1 import sounds as sounds_api
from app.models.sound import SoundTrack
from tests.conftest import FakeDb, FakeResult, TEST_USER_ID


def test_presigned_url_helper_has_route_friendly_signature():
    signature = inspect.signature(sounds_api.s3_service.generate_presigned_url)
    params = list(signature.parameters)

    assert params[0] == "object_key"


class SoundDb(FakeDb):
    def __init__(self, sound):
        super().__init__()
        self.sound = sound

    async def get(self, model, key):
        if model.__name__ == "SoundTrack":
            return self.sound
        return None

    async def execute(self, statement):
        return FakeResult(values=[])


@pytest.mark.asyncio
async def test_get_single_sound_returns_presigned_urls(monkeypatch):
    sound = SoundTrack(
        sound_id="white_noise",
        name="White Noise",
        description="Noise",
        category="white_noise",
        duration_seconds=0,
        file_url="lumi-sounds/white_noise.mp3",
        thumbnail_url="lumi-sounds/thumbnails/white_noise.png",
        is_active=True,
        sort_order=1,
    )

    def fake_presign(object_key, expiration=3600):
        return f"https://cdn.example.test/{object_key}?signed=1"

    monkeypatch.setattr(sounds_api.s3_service, "generate_presigned_url", fake_presign)

    response = await sounds_api.get_sound("white_noise", user_id=TEST_USER_ID, db=SoundDb(sound))

    assert response.file_url.startswith("https://cdn.example.test/")
    assert response.thumbnail_url.startswith("https://cdn.example.test/")
