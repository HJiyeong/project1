import openai
import os
from dotenv import load_dotenv

# .env 파일 로드
load_dotenv()

# 환경변수에서 API 키 불러오기
openai.api_key = os.getenv("OPENAI_API_KEY")

<<<<<<< HEAD
def get_cafe_recommendation(prompt: str) -> str:
    response = openai.Completion.create(
        model="text-davinci-003",
        prompt=prompt,
        max_tokens=150
    )
    return response.choices[0].text.strip()
=======
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
>>>>>>> 9ea577189afbba21e1af3a8c1bdc83eae12ab25d

def get_cafe_hashtags(description: str) -> list:
    prompt = f"이 카페 설명을 바탕으로 해시태그를 만들어줘: {description}"
    response = openai.Completion.create(
        model="text-davinci-003",
        prompt=prompt,
        max_tokens=60
    )
    hashtags = response.choices[0].text.strip().split()
    return hashtags
