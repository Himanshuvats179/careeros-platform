// =============================================================================
// Audit Service API — /api/v1/audit/**
// =============================================================================
import apiClient from './apiClient';

const AUDIT_BASE = '/api/v1/audit';

export const auditService = {
  getLogs: (params) => apiClient.get(`${AUDIT_BASE}/logs`, { params }),
  getLogById: (id) => apiClient.get(`${AUDIT_BASE}/logs/${id}`),
  getMyLogs: (params) => apiClient.get(`${AUDIT_BASE}/logs/me`, { params }),
  getLogStats: () => apiClient.get(`${AUDIT_BASE}/stats`),
  exportLogs: (params) =>
    apiClient.get(`${AUDIT_BASE}/logs/export`, {
      params,
      responseType: 'blob',
    }),
};

// =============================================================================
// Notification Service API — /api/v1/notifications/**
// =============================================================================
export const notificationService = {
  getNotifications: (params) =>
    apiClient.get('/api/v1/notifications', { params }),
  markAsRead: (id) => apiClient.patch(`/api/v1/notifications/${id}/read`),
  markAllAsRead: () => apiClient.patch('/api/v1/notifications/read-all'),
  getUnreadCount: () => apiClient.get('/api/v1/notifications/unread-count'),
  deleteNotification: (id) => apiClient.delete(`/api/v1/notifications/${id}`),
};
