// =============================================================================
// Job Service API — /api/v1/jobs/** and /api/v1/applications/**
// =============================================================================
import apiClient from './apiClient';

const JOB_BASE = '/api/v1/jobs';
const APP_BASE = '/api/v1/applications';

export const jobService = {
  // ─── Job Postings ────────────────────────────────────────────────────────
  searchJobs: (params) => apiClient.get(JOB_BASE, { params }),
  getJobById: (id) => apiClient.get(`${JOB_BASE}/${id}`),
  createJob: (data) => apiClient.post(JOB_BASE, data),
  updateJob: (id, data) => apiClient.put(`${JOB_BASE}/${id}`, data),
  deleteJob: (id) => apiClient.delete(`${JOB_BASE}/${id}`),

  // ─── My Applications (Kanban Board) ──────────────────────────────────────
  getMyApplications: () => apiClient.get(`${APP_BASE}/me`),
  applyToJob: (jobId, data) => apiClient.post(`${JOB_BASE}/${jobId}/apply`, data),
  updateApplicationStatus: (appId, status) =>
    apiClient.patch(`${APP_BASE}/${appId}/status`, { status }),
  deleteApplication: (appId) => apiClient.delete(`${APP_BASE}/${appId}`),
  getApplicationById: (appId) => apiClient.get(`${APP_BASE}/${appId}`),

  // ─── Statistics ──────────────────────────────────────────────────────────
  getApplicationStats: () => apiClient.get(`${APP_BASE}/me/stats`),
};
