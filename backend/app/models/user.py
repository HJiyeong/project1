from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from app.db.db import Base
from app.models.relations import followers_table, recommendations_table
from sqlalchemy.orm import foreign

class User(Base):
    __tablename__ = "users"

    user_id = Column(Integer, primary_key=True, index=True)
    name = Column(String)
    cafe_lists = relationship("CafeList", back_populates="owner")
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
