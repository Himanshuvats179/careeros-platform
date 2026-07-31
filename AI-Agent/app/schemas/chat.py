from typing import List, Optional
from pydantic import BaseModel, Field

class CoverLetterRequest(BaseModel):
    user_id: str
    user_name: str
    target_company: str
    target_role: str
    resume_summary: str
    job_description: str

class CoverLetterResponse(BaseModel):
    cover_letter_text: str
    key_selling_points: List[str]

class JobMatchRequest(BaseModel):
    user_id: str
    user_profile_summary: str
    job_title: str
    job_description: str

class JobMatchResponse(BaseModel):
    overall_match_score: int
    matched_skills: List[str]
    missing_skills: List[str]
    fit_recommendation: str

class AiChatMessage(BaseModel):
    role: str  # "user" or "assistant"
    content: str

class AiChatRequest(BaseModel):
    user_id: str
    session_id: str
    message: str
    history: Optional[List[AiChatMessage]] = None

class AiChatResponse(BaseModel):
    session_id: str
    reply: str
    suggested_followups: List[str]
