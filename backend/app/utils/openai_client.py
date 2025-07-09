import os
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()
key = os.getenv("OPENAI_API_KEY")
print(repr(key))
client = OpenAI(api_key=key)

def get_cafe_recommendation(prompt: str) -> str:
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "너는 내가 입력한 프롬프트를 기반으로 네이버 지도 상에 존재하는 카페를 추천해 주어야 해."},
            {"role": "user", "content": prompt}
        ]
    )
    return response.choices[0].message.content

def get_cafe_hashtags(prompt: str) -> str:
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "너는 내가 준 카페 이름에 대해서, 유효한 근거를 추론하여 주어진 형식에 맞게 해시태그를 만들어 줘야 해."},
            {"role": "user", "content": prompt}
        ]
    )
    return response.choices[0].message.content