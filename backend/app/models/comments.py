from sqlalchemy import Column, Integer, ForeignKey
from sqlalchemy.orm import relationship, String
from app.db.db import Base

class Comment(Base):
    __tablename__ = "comments"

    comment_id = Column(Integer, primary_key=True, index=True)
    feed_id = Column(Integer, ForeignKey("cafe_feeds.feed_id"))
    content = Column(String)

    feed = relationship("Feed", back_populates="comments")
    user = relationship("User")
