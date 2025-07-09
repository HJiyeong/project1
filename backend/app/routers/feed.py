from fastapi import APIRouter, Depends, Body
from sqlalchemy.orm import Session
from app.db.db import get_db
from app.schemas.cafe import PromptRequest
from app.models.cafe import Cafe
from app.models.feed import Feed
from app.schemas.cafe import CafeResponse
from app.utils.openai_client import get_cafe_recommendation, get_cafe_hashtags

router = APIRouter(prefix="/feeds", tags=["feeds"])

@router.get("/{feed_id}/comments", response_model=list[str])
def get_comments_by_feed_id(feed_id: int, db: Session = Depends(get_db)):
    feed = db.query(Feed).filter(Feed.feed_id == feed_id).first()

    comments = [comment.content for comment in feed.comments]
    return comments

@router.post("/{feed_id}/likes")
def likes(feed_id: int, likes: bool = Body(...), db: Session = Depends(get_db)):
    feed: Feed = db.query(Feed).filter(Feed.feed_id == feed_id).first()

    if (likes):
        feed.likes += 1
    else:
        feed.likes -= 1
