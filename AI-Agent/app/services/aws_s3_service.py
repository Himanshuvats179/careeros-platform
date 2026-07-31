import os
from pathlib import Path
from typing import Optional
from app.config.settings import settings
from app.utils.logger import logger

class StorageService:
    """
    Unified Storage Service supporting AWS S3 and Local Filesystem Fallback.
    Handles resume file storage, presigned URL generation, and content retrieval.
    """

    def __init__(self):
        self.use_aws = settings.USE_AWS_S3
        self.local_dir = Path(settings.LOCAL_UPLOAD_DIR)
        self.local_dir.mkdir(parents=True, exist_ok=True)
        self.s3_client = None

        if self.use_aws:
            try:
                import boto3
                self.s3_client = boto3.client(
                    's3',
                    aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
                    aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
                    region_name=settings.AWS_REGION
                )
                logger.info(f"Initialized AWS S3 Client for bucket: {settings.AWS_S3_BUCKET_NAME}")
            except Exception as e:
                logger.warn(f"Failed to initialize AWS S3 client ({e}). Falling back to local storage.")
                self.use_aws = False

    def save_file(self, file_name: str, content: bytes, content_type: str = "application/pdf") -> str:
        if self.use_aws and self.s3_client:
            try:
                self.s3_client.put_object(
                    Bucket=settings.AWS_S3_BUCKET_NAME,
                    Key=file_name,
                    Body=content,
                    ContentType=content_type
                )
                s3_url = f"https://{settings.AWS_S3_BUCKET_NAME}.s3.{settings.AWS_REGION}.amazonaws.com/{file_name}"
                logger.info(f"File successfully uploaded to AWS S3: {s3_url}")
                return s3_url
            except Exception as e:
                logger.error(f"AWS S3 upload failed ({e}). Saving locally as fallback.")

        # Local file storage fallback
        file_path = self.local_dir / file_name
        with open(file_path, "wb") as f:
            f.write(content)
        local_url = f"/uploads/{file_name}"
        logger.info(f"File saved to local storage fallback: {local_url}")
        return local_url

    def generate_presigned_url(self, file_name: str, expiration_seconds: int = 3600) -> str:
        if self.use_aws and self.s3_client:
            try:
                url = self.s3_client.generate_presigned_url(
                    'get_object',
                    Params={'Bucket': settings.AWS_S3_BUCKET_NAME, 'Key': file_name},
                    ExpiresIn=expiration_seconds
                )
                return url
            except Exception as e:
                logger.error(f"Failed to generate AWS S3 presigned URL: {e}")

        return f"/uploads/{file_name}"

storage_service = StorageService()
