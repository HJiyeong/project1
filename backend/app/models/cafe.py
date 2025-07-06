from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from app.db.db import Base
from app.models.relations import cafe_list_cafe_table, recommendations_table

class Cafe(Base):
    __tablename__ = "cafes"

    cafe_id = Column(Integer, primary_key=True)
    name = Column(String)
    image_url = Column(String)
    short_address = Column(String)

    contains = relationship(
        "CafeList",
        secondary=cafe_list_cafe_table,
        back_populates="contains"
    )

    recommends = relationship(
        "User",
        secondary=recommendations_table,
        back_populates="recommends"
    )