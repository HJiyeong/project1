from pydantic import BaseModel

class CafeCreate(BaseModel):
    name: str
    image_url: str | None = None
    short_address: str | None = None

class CafeResponse(BaseModel):
    cafe_id: int
    name: str
    image_url: str | None = None
    short_address: str | None = None

    class Config:
        orm_mode = True