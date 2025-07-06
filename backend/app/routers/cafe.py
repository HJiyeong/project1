from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.db import SessionLocal
from app.models.cafe import Cafe
from app.schemas.cafe import CafeCreate, CafeResponse

router = APIRouter(prefix="/cafes", tags=["cafes"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/", response_model=CafeResponse) # 나중에 카페 직접 추가해야 되나???
def create_cafe(cafe: CafeCreate, db: Session = Depends(get_db)):
    db_cafe = Cafe(name=cafe.name, image_url=cafe.image_url, short_address = cafe.short_address)
    db.add(db_cafe)
    db.commit()
    db.refresh(db_cafe)
    return db_cafe

@router.get("/", response_model=list[CafeResponse])
def get_cafes(db: Session = Depends(get_db)):
    return db.query(Cafe).all()

@router.get("/{cafe_id}", response_model=CafeResponse)
def get_cafe_by_id(cafe_id: int, db: Session = Depends(get_db)):
    cafe = db.query(Cafe).filter(Cafe.id == cafe_id).first()
    return cafe