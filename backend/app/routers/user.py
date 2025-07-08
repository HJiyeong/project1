from fastapi import APIRouter, Depends
from sqlalchemy import select
from sqlalchemy.orm import Session
from sqlalchemy.sql.expression import func
from app.db.db import get_db
from app.models.user import User
from app.models.relations import followers_table
from app.schemas.user import UserCreate, UserResponse
from app.utils.auth import get_current_user

router = APIRouter(prefix="/users", tags=["users"])

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

@router.get("/recommend")
def recommend_users(n: int = 10, user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    subq = (select(followers_table.c.followed_id).where(followers_table.c.follower_id == user.user_id))
    recommended_users = (
        db.query(User).filter(
            User.user_id != user.user_id,
            User.user_id.not_in(subq))
        .limit(n).all())
    return recommended_users

@router.post("/{followee_id}/follow")
def follow_user(followee_id: int, follower: User = Depends(get_current_user), db: Session = Depends(get_db)):
    followee = db.query(User).filter(User.user_id == followee_id).first()
    
    if not follower or not followee:
        return {"error": "User not found"}

    if followee in follower.follows:
        return {"message": "Already following"}

    follower.follows.append(followee)
    db.commit()
    return {"message": "following user", "followee_id": followee.user_id}

    
