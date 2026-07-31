// =============================================================================
// Profile Service API — /api/v1/profiles/**
// =============================================================================
import apiClient from './apiClient';

const PROFILE_BASE = '/api/v1/profiles';

export const profileService = {
  // ─── Core Profile ────────────────────────────────────────────────────────
  getMyProfile: () => apiClient.get(`${PROFILE_BASE}/me`),
  updateProfile: (data) => apiClient.put(`${PROFILE_BASE}/me`, data),
  getProfileById: (id) => apiClient.get(`${PROFILE_BASE}/${id}`),

  // ─── Avatar Upload ───────────────────────────────────────────────────────
  uploadAvatar: (file) => {
    const form = new FormData();
    form.append('file', file);
    return apiClient.post(`${PROFILE_BASE}/me/avatar`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  // ─── Resume Upload ───────────────────────────────────────────────────────
  uploadResume: (file) => {
    const form = new FormData();
    form.append('file', file);
    return apiClient.post(`${PROFILE_BASE}/me/resumes`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  getResumes: () => apiClient.get(`${PROFILE_BASE}/me/resumes`),
  deleteResume: (resumeId) => apiClient.delete(`${PROFILE_BASE}/me/resumes/${resumeId}`),

  // ─── Skills ─────────────────────────────────────────────────────────────
  getSkills: () => apiClient.get(`${PROFILE_BASE}/me/skills`),
  addSkill: (data) => apiClient.post(`${PROFILE_BASE}/me/skills`, data),
  updateSkill: (id, data) => apiClient.put(`${PROFILE_BASE}/me/skills/${id}`, data),
  deleteSkill: (id) => apiClient.delete(`${PROFILE_BASE}/me/skills/${id}`),

  // ─── Experience ─────────────────────────────────────────────────────────
  getExperiences: () => apiClient.get(`${PROFILE_BASE}/me/experiences`),
  addExperience: (data) => apiClient.post(`${PROFILE_BASE}/me/experiences`, data),
  updateExperience: (id, data) => apiClient.put(`${PROFILE_BASE}/me/experiences/${id}`, data),
  deleteExperience: (id) => apiClient.delete(`${PROFILE_BASE}/me/experiences/${id}`),

  // ─── Education ──────────────────────────────────────────────────────────
  getEducations: () => apiClient.get(`${PROFILE_BASE}/me/educations`),
  addEducation: (data) => apiClient.post(`${PROFILE_BASE}/me/educations`, data),
  updateEducation: (id, data) => apiClient.put(`${PROFILE_BASE}/me/educations/${id}`, data),
  deleteEducation: (id) => apiClient.delete(`${PROFILE_BASE}/me/educations/${id}`),

  // ─── Projects ───────────────────────────────────────────────────────────
  getProjects: () => apiClient.get(`${PROFILE_BASE}/me/projects`),
  addProject: (data) => apiClient.post(`${PROFILE_BASE}/me/projects`, data),
  updateProject: (id, data) => apiClient.put(`${PROFILE_BASE}/me/projects/${id}`, data),
  deleteProject: (id) => apiClient.delete(`${PROFILE_BASE}/me/projects/${id}`),

  // ─── Certifications ─────────────────────────────────────────────────────
  getCertifications: () => apiClient.get(`${PROFILE_BASE}/me/certifications`),
  addCertification: (data) => apiClient.post(`${PROFILE_BASE}/me/certifications`, data),
  deleteCertification: (id) => apiClient.delete(`${PROFILE_BASE}/me/certifications/${id}`),
};
