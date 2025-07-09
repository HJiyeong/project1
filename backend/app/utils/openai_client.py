
import os
import openai
from dotenv import load_dotenv


# .env 파일 로드
load_dotenv()

openai.api_key=os.getenv("OPENAI_API_KEY")

def get_cafe_recommendation(prompt: str) -> list[str]:
    response = openai.Completion.create(
        model="text-davinci-003",
        prompt=prompt,
        max_tokens=150
    )
    raw = response.choices[0].text.strip()

def get_cafe_hashtags(description: str) -> list:
    prompt = f"이 카페 설명을 바탕으로 해시태그를 만들어줘: {description}"
    response = client.chat.Completion.create(
        model="text-davinci-003",
        prompt=prompt,
        max_tokens=60
    )
    hashtags = response.choices[0].text.strip().split()
    return hashtags
