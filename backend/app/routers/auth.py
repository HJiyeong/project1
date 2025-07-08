from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.db import get_db
from app.models.user import User
from app.schemas.user import UserCreate, UserLogin, UserResponse
from app.utils.auth import get_password_hash, verify_password, create_jwt_token, get_current_user

router = APIRouter(prefix="/auth")

@router.post("/signup")
def signup(user_data: UserCreate, db: Session = Depends(get_db)):
    if db.query(User).filter(User.email == user_data.email).first():
        raise HTTPException(status_code=400, detail="email already exists")
    
    new_user = User(
        email=user_data.email,
        name=user_data.name,
        hashed_password=get_password_hash(user_data.password)
    )
    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    return {"msg": "회원가입 성공", "user_id": new_user.user_id}

@router.post("/login")
def login(login_data: UserLogin, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.email == login_data.email).first()
    if not user or not verify_password(login_data.password, user.hashed_password):
        raise HTTPException(status_code=401, detail="이메일 또는 비밀번호가 틀렸습니다.")

    token = create_jwt_token(user.user_id)
    return {"access_token": token, "token_type": "bearer"}

@router.get("/me", response_model=UserResponse)
def read_current_user(current_user: User = Depends(get_current_user)):
    return current_user