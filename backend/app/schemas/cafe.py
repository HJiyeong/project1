from pydantic import BaseModel
from app.schemas.user import UserResponse

'''
class CafeCreate(BaseModel):
    name: str
    image_url: str | None = None
    short_address: str | None = None
'''
class PromptRequest(BaseModel):
    prompt: str

class CafeResponse(BaseModel):
    cafe_id: int
    name: str
    image_url: str | None = None
    short_address: str | None = None
    
    cafe_url: str | None = None
    cafe_introduce: str | None = None
    amenities: str | None = None

    class Config:
        orm_mode = True

class FeedResponse(BaseModel):
    feed_id: int
    user: UserResponse
    cafe: CafeResponse
    likes: int

    class Config:
        orm_mode = True