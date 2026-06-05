import boto3

from app.core.config import settings


class S3Service:
    def __init__(self):
        self.bucket = settings.S3_SOUNDS_BUCKET

        self.client = boto3.client(
            "s3",
            aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
            aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
            region_name=settings.AWS_REGION,
        )

    def generate_presigned_url(
        self,
        object_key: str,
        expiration: int = 3600,
    ) -> str:
        return self.client.generate_presigned_url(
            ClientMethod="get_object",
            Params={
                "Bucket": self.bucket,
                "Key": object_key,
            },
            ExpiresIn=expiration,
        )


s3_service = S3Service()