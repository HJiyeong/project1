from sqlalchemy import Column, Integer, Table, ForeignKey
from sqlalchemy.orm import relationship
from datetime import datetime
from app.db.db import Base
from app.models.relations import cafe_list_cafe_table, recommendations_table


class Feed(Base):
    __tablename__ = "cafe_feeds"

    feed_id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.user_id"))
    cafe_id = Column(Integer, ForeignKey("cafes.cafe_id"))
    created_at = Column(DateTime, default=datetime.utcnow)

    likes = Column(Integer)
    
    comments = relationship("Comment", back_populates="feed", cascade="all, delete-orphan")
    user = relationship("User", back_populates="feeds")
    cafe = relationship("Cafe")
