// =============================================================================
// Auth Service API — /api/v1/auth/**
// =============================================================================
import apiClient from './apiClient';

const AUTH_BASE = '/api/v1/auth';

export const authService = {
  /**
   * Register a new user account
   * @param {{ firstName, lastName, email, password }} data
   */
  register: (data) => apiClient.post(`${AUTH_BASE}/register`, data),

  /**
   * Login with email + password → returns { token, user }
   * @param {{ email, password }} data
   */
  login: (data) => apiClient.post(`${AUTH_BASE}/login`, data),

  /**
   * Logout — blacklists token in Redis
   */
  logout: () => apiClient.post(`${AUTH_BASE}/logout`),

  /**
   * Refresh JWT access token
   */
  refreshToken: () => apiClient.post(`${AUTH_BASE}/refresh`),

  /**
   * Get currently authenticated user info
   */
  me: () => apiClient.get(`${AUTH_BASE}/me`),

  /**
   * Change password
   * @param {{ currentPassword, newPassword }} data
   */
  changePassword: (data) => apiClient.post(`${AUTH_BASE}/change-password`, data),
};
