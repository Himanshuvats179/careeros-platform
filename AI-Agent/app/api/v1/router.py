from fastapi import APIRouter
from app.api.v1.rag_router import router as rag_router
from app.api.v1.resume_router import router as resume_router
from app.api.v1.career_router import router as career_router
from app.api.v1.interview_router import router as interview_router
from app.api.v1.chat_router import router as chat_router

api_v1_router = APIRouter()

api_v1_router.include_router(rag_router)
api_v1_router.include_router(resume_router)
api_v1_router.include_router(career_router)
api_v1_router.include_router(interview_router)
api_v1_router.include_router(chat_router)
