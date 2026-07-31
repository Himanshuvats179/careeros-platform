// =============================================================================
// CareerOS Frontend — Centralized API Service Layer
// All microservice calls go through the API Gateway at :8443
// =============================================================================

import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:8443';

// ─── Axios Instance ─────────────────────────────────────────────────────────
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

// ─── Request Interceptor: inject JWT + tracing header ────────────────────────
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('careeros_jwt');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    config.headers['X-Correlation-Id'] = `web-${Date.now()}-${Math.random().toString(36).slice(2, 9)}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// ─── Response Interceptor: handle 401 auto-logout ────────────────────────────
apiClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('careeros_jwt');
      localStorage.removeItem('careeros_user');
      window.location.href = '/auth';
    }
    const message =
      error.response?.data?.message ||
      error.response?.data?.error ||
      error.message ||
      'An unexpected error occurred';
    return Promise.reject(new Error(message));
  }
);

export default apiClient;
