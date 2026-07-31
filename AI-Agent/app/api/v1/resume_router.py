from fastapi import APIRouter, HTTPException, status
from app.schemas.resume import (
    ResumeAnalyzeRequest, ResumeAnalyzeResponse,
    ResumeImproveRequest, ResumeImproveResponse,
    AtsScoreRequest, AtsScoreResponse
)
from app.schemas.common import APIResponse
from app.services.ai_service import ai_service

router = APIRouter(prefix="/resume", tags=["Resume AI Agents"])

@router.post("/analyze", response_model=APIResponse[ResumeAnalyzeResponse])
async def analyze_resume(request: ResumeAnalyzeRequest):
    try:
        data = await ai_service.analyze_resume(request.user_id, request.resume_text, request.target_role or "Software Engineer")
        return APIResponse(message="Resume analyzed successfully", data=ResumeAnalyzeResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/improve", response_model=APIResponse[ResumeImproveResponse])
async def improve_resume(request: ResumeImproveRequest):
    try:
        data = await ai_service.improve_resume(request.user_id, request.resume_text, request.target_job_description or "")
        return APIResponse(message="Resume improved successfully", data=ResumeImproveResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/ats-score", response_model=APIResponse[AtsScoreResponse])
async def calculate_ats_score(request: AtsScoreRequest):
    try:
        data = await ai_service.calculate_ats_score(request.user_id, request.resume_text, request.job_description)
        return APIResponse(message="ATS score calculated successfully", data=AtsScoreResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))
