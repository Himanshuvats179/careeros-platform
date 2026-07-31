// =============================================================================
// AI Agent Service API — /api/v1/ai/**  (proxied to FastAPI :8000)
// =============================================================================
import apiClient from './apiClient';

const AI_BASE = '/api/v1/ai';

export const aiService = {
  // ─── Resume Analysis ─────────────────────────────────────────────────────
  analyzeResume: (resumeId) =>
    apiClient.post(`${AI_BASE}/resume/analyze`, { resume_id: resumeId }),

  improveResume: (resumeText) =>
    apiClient.post(`${AI_BASE}/resume/improve`, { resume_text: resumeText }),

  getAtsScore: (resumeText, jobDescription) =>
    apiClient.post(`${AI_BASE}/ats-score`, {
      resume_text: resumeText,
      job_description: jobDescription,
    }),

  // ─── Career Intelligence ─────────────────────────────────────────────────
  generateCareerRoadmap: (data) =>
    apiClient.post(`${AI_BASE}/career-roadmap`, data),

  analyzeSkillGap: (data) =>
    apiClient.post(`${AI_BASE}/skill-gap`, data),

  getLearningPath: (targetRole) =>
    apiClient.post(`${AI_BASE}/learning-path`, { target_role: targetRole }),

  // ─── Interview Preparation ───────────────────────────────────────────────
  generateInterviewQuestions: (data) =>
    apiClient.post(`${AI_BASE}/interview/questions`, data),

  submitMockAnswer: (data) =>
    apiClient.post(`${AI_BASE}/mock-interview`, data),

  // ─── Cover Letter & Job Matching ─────────────────────────────────────────
  generateCoverLetter: (data) =>
    apiClient.post(`${AI_BASE}/cover-letter`, data),

  matchJobs: (profileData) =>
    apiClient.post(`${AI_BASE}/job-match`, profileData),

  // ─── AI Chat Coach ───────────────────────────────────────────────────────
  chat: (message, conversationHistory = []) =>
    apiClient.post(`${AI_BASE}/chat`, {
      message,
      conversation_history: conversationHistory,
    }),

  // ─── Health ──────────────────────────────────────────────────────────────
  health: () => apiClient.get(`${AI_BASE}/health`),
};
