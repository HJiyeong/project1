from sqlalchemy import text
from sqlalchemy.orm import Session
from app.models.user import User
from app.models.cafe import Cafe
from app.models.cafe_list import CafeList
from app.db.db import SessionLocal

def init_dummy_data():
    db: Session = SessionLocal()

    if db.query(User).first():
        print("Dummy data already exists.")
        db.close()
        return

    cafe_names = [
        "스타벅스 대전유성구청점","홀리크로스","카페스테이인터뷰 대전신세계 Art&Science점",
        "메가커피 대전충남대정문점","카페1","카페2",
        "카페3","카페 에이트"
    ]
    cafe_addresses = [
        "대전 유성구 어은동","대전 서구 둔산동","대전 유성구 도룡동",
        "대전 유성구 궁동","대전 유성구 어은동", "대전 유성구 어은동",
        "대전 유성구 어은동","대전 유성구 궁동"
    ]

    BASE_URL = "http://10.0.2.2:8000"

    cafes = [
        Cafe(
            name=cafe_names[i],
            image_url=f"{BASE_URL}/static/img_cafe_sample{i+1}.jpg",
            short_address=cafe_addresses[i]
        )
        for i in range(8)
    ]

    db.add_all(cafes)
    db.flush()

    cafe_list_names = ["카공", "데이트", "인스타", "저가", "살려주세요"]
    cafe_list_contents = [
        [1,4,5,7,8], [2,3,8], [2,3,4,6,7,8], [1,4,5], [1,2,3,4,5,6,7,8]
    ]
    cafe_lists = []
    for i in range(5):
        cafe_ids = cafe_list_contents[i]
        included_cafes = [cafes[j-1] for j in cafe_ids]
        clist = CafeList(
            name=cafe_list_names[i],
            image_url=included_cafes[0].image_url,
            contains=included_cafes
        )
        cafe_lists.append(clist)
    db.add_all(cafe_lists)
    db.flush()

    user_names = ["황지영", "신우혁", "A", "B"]
    cafe_list_indices = [
        [1,2,3], [2,4,5], [], []
    ]
    recommend_indices = [
        [1,4,5,6], [3,4,5,6], [1,2], [1,5,7,8]
    ]

    users = []
    for i in range(4):
        lists = [cafe_lists[j-1] for j in cafe_list_indices[i]]
        recs = [cafes[j-1] for j in recommend_indices[i]]
        u = User(name=user_names[i], cafe_lists=lists, recommends=recs)
        users.append(u)
    db.add_all(users)
    db.flush()

    users[0].follows.extend([users[1], users[2], users[3]])

    db.commit()
    db.close()