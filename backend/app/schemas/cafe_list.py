from typing import List
from pydantic import BaseModel
from app.schemas.cafe import CafeResponse

class CafeListCreate(BaseModel):
    name: str
    image_url: str | None = None
    user_id: str

class CafeListResponse(BaseModel):
    list_id: int
    name: str
    image_url: str | None
    user_id: int
    contains: List[CafeResponse] = []

    class Config:
        orm_mode = True

class CafeAddRequest(BaseModel):
    cafe_id: int