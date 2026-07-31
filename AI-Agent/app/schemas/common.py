from typing import Generic, TypeVar, Optional
from pydantic import BaseModel, Field
from datetime import datetime

T = TypeVar("T")

class APIResponse(BaseModel, Generic[T]):
    success: bool = True
    message: str = "Request processed successfully"
    data: Optional[T] = None
    timestamp: datetime = Field(default_factory=datetime.utcnow)
