import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

import { AuthPage } from '../pages/AuthPage';
import { DashboardPage } from '../pages/DashboardPage';
import { ProfilePage } from '../pages/ProfilePage';
import { ResumePage } from '../pages/ResumePage';
import { CareerRoadmapPage } from '../pages/CareerRoadmapPage';
import { InterviewPage } from '../pages/InterviewPage';
import { AiChatPage } from '../pages/AiChatPage';
import { JobTrackerPage } from '../pages/JobTrackerPage';
import { AuditLogsPage } from '../pages/AuditLogsPage';
import { SettingsPage } from '../pages/SettingsPage';
import { NotificationsPage } from '../pages/NotificationsPage';

const ProtectedRoute = ({ children }) => {
  const { token, loading } = useAuth();
  if (loading) return null;
  if (!token) return <Navigate to="/auth" replace />;
  return children;
};

export const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/auth" element={<AuthPage />} />
      <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
      <Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
      <Route path="/resume" element={<ProtectedRoute><ResumePage /></ProtectedRoute>} />
      <Route path="/career-roadmap" element={<ProtectedRoute><CareerRoadmapPage /></ProtectedRoute>} />
      <Route path="/interview" element={<ProtectedRoute><InterviewPage /></ProtectedRoute>} />
      <Route path="/ai-chat" element={<ProtectedRoute><AiChatPage /></ProtectedRoute>} />
      <Route path="/job-tracker" element={<ProtectedRoute><JobTrackerPage /></ProtectedRoute>} />
      <Route path="/audit-logs" element={<ProtectedRoute><AuditLogsPage /></ProtectedRoute>} />
      <Route path="/settings" element={<ProtectedRoute><SettingsPage /></ProtectedRoute>} />
      <Route path="/notifications" element={<ProtectedRoute><NotificationsPage /></ProtectedRoute>} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
};
