from typing import List, Optional
from pydantic import BaseModel, Field

class InterviewQuestionsRequest(BaseModel):
    user_id: str
    target_role: str
    experience_level: str = "MID"
    focus_areas: List[str] = ["Technical", "System Design", "Behavioral"]

class QuestionItem(BaseModel):
    id: int
    category: str
    question: str
    expected_answer_keypoints: List[str]
    difficulty: str

class InterviewQuestionsResponse(BaseModel):
    target_role: str
    questions: List[QuestionItem]

class MockInterviewRequest(BaseModel):
    user_id: str
    question: str
    user_answer: str
    target_role: str

class MockInterviewResponse(BaseModel):
    question: str
    user_answer: str
    score_out_of_10: int
    strengths: List[str]
    improvements: List[str]
    ideal_sample_answer: str
