from typing import List
from pydantic import BaseModel
from app.schemas.cafe import CafeResponse

class CafeListCreate(BaseModel):
    name: str
    user_id: int

class CafeListResponse(BaseModel):
    list_id: int
    name: str
    image_url: str | None
    user_id: int
    contains: List[CafeResponse] = []

    class Config:
        orm_mode = True

class CafeChangeNameRequest(BaseModel):
    list_id: int
    name: str
