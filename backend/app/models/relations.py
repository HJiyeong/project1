from sqlalchemy import Column, Integer, ForeignKey, Table
from app.db.db import Base

cafe_list_cafe_table = Table(
    "cafe_list_cafe",
    Base.metadata,
    Column("list_id", Integer, ForeignKey("cafe_lists.list_id"), primary_key=True),
    Column("cafe_id", Integer, ForeignKey("cafes.cafe_id"), primary_key=True)
)

followers_table = Table(
    "followers",
    Base.metadata,
    Column("follower_id", Integer, ForeignKey("users.user_id"), primary_key=True),
    Column("followed_id", Integer, ForeignKey("users.user_id"), primary_key=True)
)

recommendations_table = Table(
    "recommendations",
    Base.metadata,
    Column("user_id", Integer, ForeignKey("users.user_id"), primary_key=True),
    Column("cafe_id", Integer, ForeignKey("cafes.cafe_id"), primary_key=True)
)
