from fastapi import APIRouter, HTTPException, status
from app.schemas.chat import (
    CoverLetterRequest, CoverLetterResponse,
    JobMatchRequest, JobMatchResponse,
    AiChatRequest, AiChatResponse
)
from app.schemas.common import APIResponse
from app.services.ai_service import ai_service

router = APIRouter(tags=["Chat & Job Matching AI Agents"])

@router.post("/cover-letter", response_model=APIResponse[CoverLetterResponse])
async def generate_cover_letter(request: CoverLetterRequest):
    try:
        data = await ai_service.generate_cover_letter(
            request.user_id, request.user_name, request.target_company,
            request.target_role, request.resume_summary, request.job_description
        )
        return APIResponse(message="Cover letter generated successfully", data=CoverLetterResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/job-match", response_model=APIResponse[JobMatchResponse])
async def match_job(request: JobMatchRequest):
    try:
        data = await ai_service.match_job(request.user_id, request.user_profile_summary, request.job_title, request.job_description)
        return APIResponse(message="Job match analysis completed", data=JobMatchResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/chat", response_model=APIResponse[AiChatResponse])
async def process_chat(request: AiChatRequest):
    try:
        data = await ai_service.process_chat(request.user_id, request.session_id, request.message, request.history)
        return APIResponse(message="Chat response generated successfully", data=AiChatResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))
