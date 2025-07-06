from pydantic import BaseModel
from typing import List
from app.schemas.cafe import CafeResponse
from app.schemas.cafe_list import CafeListResponse



class UserCreate(BaseModel):
    name: str

class UserResponse(BaseModel):
    user_id: int
    name: str
    cafe_lists: List[CafeListResponse] = []
    recommends: List[CafeResponse] = []
    follows: List["UserResponse"] = []

    class Config:
        orm_mode = True