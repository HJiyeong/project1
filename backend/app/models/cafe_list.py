from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from app.db.db import Base
from app.models.relations import cafe_list_cafe_table

class CafeList(Base):
    __tablename__ = "cafe_lists"

    list_id = Column(Integer, primary_key=True)
    name = Column(String)
    image_url = Column(String)


    user_id = Column(Integer, ForeignKey("users.user_id"))

    owner = relationship("User", back_populates="cafe_lists")
    contains = relationship(
        "Cafe",
        secondary=cafe_list_cafe_table,
        back_populates="contains"
    )
