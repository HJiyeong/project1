from datetime import datetime
from xmlrpc.client import Boolean, DateTime
from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from app.db.db import Base
from app.models.relations import followers_table, recommendations_table
from sqlalchemy.orm import foreign

class User(Base):
    __tablename__ = "users"

    user_id = Column(Integer, primary_key=True, index=True)
    name = Column(String)
    profile_image = Column(String, nullable = True, default = None)

    cafe_lists = relationship("CafeList", back_populates="owner")
    email = Column(String, unique=True, nullable=False)
    hashed_password = Column(String)

    # is_active = Column(Boolean, default=True)
    # created_at = Column(DateTime, default=datetime.utcnow)

    recommends = relationship(
        "Cafe",
        secondary=recommendations_table,
        back_populates="recommends"
    )

    follows = relationship(
        "User",
        secondary=followers_table,
        primaryjoin=foreign(followers_table.c.follower_id) == user_id,
        secondaryjoin=foreign(followers_table.c.followed_id) == user_id,
        backref="followers"
    )

    feeds = relationship("Feed", back_populates="user", cascade="all, delete")
