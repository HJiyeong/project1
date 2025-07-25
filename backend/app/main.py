import app.models
from fastapi import FastAPI
from app.init_dummy_data import init_dummy_data
from app.routers import auth, user, cafe, cafe_list, feed
from app.db.db import Base, engine

Base.metadata.create_all(bind=engine)

app = FastAPI()
app.include_router(auth.router)
app.include_router(user.router)
app.include_router(cafe.router)
app.include_router(feed.router)
app.include_router(cafe_list.router)

@app.on_event("startup")
def startup_event():
    init_dummy_data()

@app.get("/")
def read_root():
    return {"Hello": "World"}

