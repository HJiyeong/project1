from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from app.db.db import get_db
from app.schemas.cafe import PromptRequest
from app.models.cafe import Cafe
from app.schemas.cafe import CafeResponse
from app.utils.openai_client import get_cafe_recommendation, get_cafe_hashtags

router = APIRouter(prefix="/cafes", tags=["cafes"])

'''
@router.post("/", response_model=CafeResponse)
def create_cafe(cafe: CafeCreate, db: Session = Depends(get_db)):
    db_cafe = Cafe(name=cafe.name, image_url=cafe.image_url, short_address = cafe.short_address)
    db.add(db_cafe)
    db.commit()
    db.refresh(db_cafe)
    return db_cafe
'''
@router.get("/default", response_model=list[CafeResponse])
def get_default_cafes(db: Session = Depends(get_db)):
    return db.query(Cafe).limit(20).all()

@router.get("/", response_model=list[CafeResponse])
def get_cafes(db: Session = Depends(get_db)):
    return db.query(Cafe).all()

@router.get("/{cafe_id}", response_model=CafeResponse)
def get_cafe_by_id(cafe_id: int, db: Session = Depends(get_db)):
    cafe = db.query(Cafe).filter(Cafe.cafe_id == cafe_id).first()
    return cafe

@router.post("/recommend", response_model=list[CafeResponse])
def recommend_cafes(prompt: PromptRequest, db: Session = Depends(get_db)):
    prompt_ = prompt.prompt
    prompt_result = [name.strip() for name in get_cafe_recommendation(prompt_).split(",")]
    result = []
    for name in prompt_result:
        cafe = db.query(Cafe).filter(Cafe.name == name).first()
        if not cafe:
            continue
        result.append(cafe)
        if (len(result) == 10):
            break
    return result

@router.post("/hashtag")
def get_hashtag(prompt: PromptRequest):
    return get_cafe_hashtags(prompt.prompt)

@router.get("/default", response_model=list[CafeResponse])
def get_default_cafes(db: Session = Depends(get_db)):
    return db.query(Cafe).limit(20).all()

