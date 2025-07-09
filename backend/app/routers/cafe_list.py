from fastapi import APIRouter, Depends, Body
from sqlalchemy.orm import Session
from app.db.db import get_db
from app.models.user import User
from app.models.cafe import Cafe
from app.models.cafe_list import CafeList
from app.schemas.cafe import CafeResponse
from app.schemas.cafe_list import CafeListCreate, CafeListResponse, CafeChangeNameRequest

router = APIRouter(prefix="/lists", tags=["lists"])

@router.post("/", response_model=CafeListResponse)
def create_list(cafe_list: CafeListCreate = Body(...), db: Session = Depends(get_db)):
    db_list = CafeList(
        name=cafe_list.name,
        image_url="",
        user_id=cafe_list.user_id
    )
    db.add(db_list)
    db.commit()
    db.refresh(db_list)
    return db_list

@router.put("/{list_id}/set-default-image")
def set_list_image_to_default(
    list_id: int,
    db: Session = Depends(get_db)
):
    cafe_list = db.query(CafeList).filter(CafeList.list_id == list_id).first()
    first_cafe = db.query(Cafe).filter(Cafe.cafe_id == list_id).order_by(Cafe.cafe_id).first()
    cafe_list.image_url = first_cafe.image_url
    db.commit()
    db.refresh(cafe_list)

    return cafe_list


@router.get("/{user_id}", response_model=list[CafeListResponse])
def get_lists(user_id: int, db: Session = Depends(get_db)):
    return db.query(CafeList).filter(CafeList.user_id == user_id).all()

@router.get("/{list_id}/get_list", response_model=CafeListResponse)
def get_list_by_id(list_id: int, db: Session = Depends(get_db)):
    return db.query(CafeList).filter(CafeList.list_id == list_id).first()

@router.get("/{list_id}/get_candidates", response_model=list[CafeResponse])
def get_cafe_candidates(list_id: int, db: Session = Depends(get_db)):
    cafe_list: CafeList = db.query(CafeList).filter(CafeList.list_id == list_id).first()
    user: User = cafe_list.owner
    
    recommended = set(user.recommends)
    already_contained = set(cafe_list.contains)
    candidates = list(recommended - already_contained)

    return candidates

@router.post("/{list_id}/add_cafe")
def add_cafe_to_list(list_id: int, cafe_id: int = Body(...), db: Session = Depends(get_db)):
    cafe_list = db.query(CafeList).filter(CafeList.list_id == list_id).first()
    if not cafe_list:
        return {"error": "CafeList not found"}
    
    cafe = db.query(Cafe).filter(Cafe.cafe_id == cafe_id).first()
    if not cafe:
        return {"error": "Cafe not found"}


    if cafe in cafe_list.contains:
        return {"message": "Cafe already in list"}

    cafe_list.contains.append(cafe)
    db.commit()
    return {"message": "Cafe added to list"}

@router.put("/change-name")
def change_list_name(req: CafeChangeNameRequest = Body(...), db: Session = Depends(get_db)):
    cafe_list: CafeList = db.query(CafeList).filter(CafeList.list_id == req.list_id).first()
    cafe_list.name = req.name
    print(req.name)
    db.commit()
    db.refresh(cafe_list)
    return cafe_list

@router.put("/{list_id}/delete")
def delete_list(list_id: int, db: Session = Depends(get_db)):
    cafe_list: CafeList = db.query(CafeList).filter(CafeList.list_id == list_id).first()
    db.delete(cafe_list)
    db.commit()