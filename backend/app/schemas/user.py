from datetime import datetime
from pydantic import BaseModel, EmailStr
from typing import List, Optional
from app.schemas.cafe import CafeResponse
from app.schemas.cafe_list import CafeListResponse



class UserCreate(BaseModel):
    email: EmailStr
    name: str
    password: str

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class UserResponse(BaseModel):
    user_id: int
    name: str
    email: EmailStr
    profile_image: Optional[str] = None

    # is_active: bool
    # created_at: datetime
    
    cafe_lists: List[CafeListResponse] = []
    recommends: List[CafeResponse] = []
    follows: List["UserResponse"] = []

    class Config:
        orm_mode = True