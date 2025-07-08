import os
import openai

openai.api_key = 'HELLO WORLD!'

def get_cafe_recommendation(prompt: str) -> str:
    response = openai.ChatCompletion.create(
        model="gpt-4",
        messages=[
            {"role": "system", "content": "대충 너는 카페 추천 전문가라는 내용"},
            {"role": "user", "content": prompt}
        ]
    )
    return response['choices'][0]['message']['content']

# 대충 프롬프트는 이름 상위 15개만 추려서 네이버 지도 기준 정확한 상호명을 "상호명1, 상호명2, 상호명3, ..." 이런 식으로 짧게 답변해 달라는 내용