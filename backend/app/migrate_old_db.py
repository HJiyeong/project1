import sqlite3
from app.db.db import SessionLocal
from app.models.cafe import Cafe

# 기존 DB 경로
old_conn = sqlite3.connect("../old_app.db")  # 경로 주의! 상위 폴더로
cursor = old_conn.cursor()
cursor.execute("SELECT name, address, url, img, description, services FROM cafes")
rows = cursor.fetchall()

# 새 DB에 삽입
db = SessionLocal()
for row in rows:
    cafe = Cafe(
        name=row[0],
        short_address=row[1],
        cafe_url=row[2],
        image_url=row[3],
        cafe_introduce=row[4],
        amenities=row[5]
    )
    db.add(cafe)

db.commit()
db.close()
old_conn.close()

print("✅ 데이터 이식 완료!")
