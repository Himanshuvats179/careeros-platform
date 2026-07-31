from typing import List, Optional
from pydantic import BaseModel, Field

class ResumeAnalyzeRequest(BaseModel):
    user_id: str = Field(..., description="UUID of the user")
    resume_text: str = Field(..., description="Extracted resume text content")
    target_role: Optional[str] = Field(None, description="Desired target job title")

class SkillAnalysis(BaseModel):
    category: str
    skills: List[str]

class ResumeAnalyzeResponse(BaseModel):
    summary: str
    strengths: List[str]
    weaknesses: List[str]
    detected_skills: List[SkillAnalysis]
    formatting_score: int = Field(..., ge=0, le=100)

class ResumeImproveRequest(BaseModel):
    user_id: str
    resume_text: str
    target_job_description: Optional[str] = None

class ActionItem(BaseModel):
    section: str
    original: str
    improved: str
    rationale: str

class ResumeImproveResponse(BaseModel):
    improved_resume_text: str
    action_items: List[ActionItem]
    overall_impact_score: int

class AtsScoreRequest(BaseModel):
    user_id: str
    resume_text: str
    job_description: str

class AtsScoreResponse(BaseModel):
    match_percentage: int
    missing_keywords: List[str]
    matched_keywords: List[str]
    formatting_issues: List[str]
    recommendations: List[str]
