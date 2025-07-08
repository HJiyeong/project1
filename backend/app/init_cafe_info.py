import json
from app.models.cafe import Cafe
from sqlalchemy.orm import Session
from app.database import SessionLocal
from app.models import Store
from app.schemas import StoreCreate

with open("stores.json", "r", encoding="utf-8") as f:
    store_data = json.load(f)

def init_cafe_info():
    db: Session = SessionLocal()
    cafes = []
    for item in store_data:
        
        cafes.append(Cafe(
            name=item["name"],
            image_url=item["image_url"],
            short_address=item["short_address"],
            cafe_url=item["cafe_url"],
            cafe_introduce=item["cafe_introduce"],
            amenities=item["amenities"]
            # amenities=",".join(item.get("amenities", []))
        ))
        
    db.add_all(cafes)
    db.commit()
    db.close()