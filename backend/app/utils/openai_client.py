import os
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()
key = os.getenv("OPENAI_API_KEY")
print(repr(key))
client = OpenAI(api_key=key)

def get_cafe_recommendation(prompt: str) -> list[str]:
    response = client.chat.completions.create(
        model="gpt-4.1-nano",
        messages=[
            {"role": "system", "content": "오직 카페 이름만 콤마(,)로 구분해서 출력해줘. 다른 말은 절대 하지 마."},
            {"role": "user", "content": prompt}
        ]
    )
    raw = response.choices[0].message.content
    print("🧠 GPT 응답 (raw):", raw)

    # ✅ 콤마(,)가 포함된 줄만 추출해서 리스트로 변환
    for line in raw.splitlines():
        if line.count(",") >= 2:  # 카페 여러 개가 있는 줄
            return [name.strip() for name in line.split(",") if name.strip()]

    return []

def get_cafe_hashtags(prompt: str) -> str:
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "너는 내가 준 카페 이름에 대해서, 유효한 근거를 추론하여 주어진 형식에 맞게 해시태그를 만들어 줘야 해."},
            {"role": "user", "content": prompt}
        ]
    )
    return response.choices[0].message.content