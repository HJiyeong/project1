from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.db import SessionLocal
from app.models.cafe import Cafe
from app.models.cafe_list import CafeList
from app.schemas.cafe_list import CafeListCreate, CafeListResponse, CafeAddRequest

router = APIRouter(prefix="/lists", tags=["lists"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/", response_model=CafeListResponse)
def create_list(cafe_list: CafeListCreate, db: Session = Depends(get_db)):
    db_list = CafeList(
        name=cafe_list.name,
        image_url=cafe_list.image_url,
        user_id=cafe_list.user_id
    )
    db.add(db_list)
    db.commit()
    db.refresh(db_list)
    return db_list

@router.get("/{user_id}", response_model=list[CafeListResponse])
def get_lists(user_id: int, db: Session = Depends(get_db)):
    return db.query(CafeList).filter(CafeList.user_id == user_id).all()

@router.post("/{list_id}/add_cafe")
def add_cafe_to_list(list_id: int, req: CafeAddRequest, db: Session = Depends(get_db)):
    cafe_list = db.query(CafeList).filter(CafeList.id == list_id).first()
    if not cafe_list:
        return {"error": "CafeList not found"}
    
    cafe = db.query(Cafe).filter(Cafe.id == req.cafe_id).first()
    if not cafe:
        return {"error": "Cafe not found"}


    if cafe in cafe_list.cafes:
        return {"message": "Cafe already in list"}

    cafe_list.cafes.append(cafe)
    db.commit()
    return {"message": "Cafe added to list"}

@router.get("/{user_id}", response_model=list[CafeListResponse])
def get_lists(user_id: int, db: Session = Depends(get_db)):
    return db.query(CafeList).filter(CafeList.user_id == user_id).all()

