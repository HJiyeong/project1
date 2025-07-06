from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.db import SessionLocal
from app.models.user import User
from app.schemas.user import UserCreate, UserResponse

router = APIRouter(prefix="/users", tags=["users"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/", response_model=UserResponse)
def create_user(user: UserCreate, db: Session = Depends(get_db)):
    db_user = User(name=user.name)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

@router.get("/", response_model=list[UserResponse])
def get_users(db: Session = Depends(get_db)):
    return db.query(User).all()

@router.post("/{follower_id}/follow")
def follow_user(follower_id: int, followee_id: int, db: Session = Depends(get_db)):
    follower = db.query(User).filter(User.id == follower_id).first()
    followee = db.query(User).filter(User.id == followee_id).first()

    
    if not follower or not followee:
        return {"error": "User not found"}

    if followee in follower.following:
        return {"message": "Already following"}

    follower.following.append(followee)
    db.commit()
    return {"message": "following user", "followee_id": followee.id}
