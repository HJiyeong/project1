from pydantic import BaseModel
from app.schemas.user import UserResponse
from app.schemas.cafe import CafeResponse

class FeedResponse(BaseModel):
    feed_id: int
    user: UserResponse
    cafe: CafeResponse
    likes: int

    class Config:
        orm_mode = True