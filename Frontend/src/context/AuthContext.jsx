import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('careeros_user');
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  });
  const [token, setToken] = useState(localStorage.getItem('careeros_jwt') || null);
  const [loading, setLoading] = useState(true);
  const [theme, setTheme] = useState(localStorage.getItem('careeros_theme') || 'dark');

  // ─── Theme ──────────────────────────────────────────────────────────────
  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('careeros_theme', theme);
  }, [theme]);

  // ─── Restore session from token on app load ──────────────────────────────
  useEffect(() => {
    const restoreSession = async () => {
      const storedToken = localStorage.getItem('careeros_jwt');
      if (!storedToken) {
        setLoading(false);
        return;
      }
      try {
        // Verify token is still valid by fetching /auth/me
        const response = await authService.me();
        const userData = response?.data || response;
        setUser(userData);
        setToken(storedToken);
        localStorage.setItem('careeros_user', JSON.stringify(userData));
      } catch {
        // Token expired or invalid — clear session
        localStorage.removeItem('careeros_jwt');
        localStorage.removeItem('careeros_user');
        setToken(null);
        setUser(null);
      } finally {
        setLoading(false);
      }
    };
    restoreSession();
  }, []);

  // ─── Login ───────────────────────────────────────────────────────────────
  const login = useCallback(async (credentials) => {
    const response = await authService.login(credentials);
    const { token: jwtToken, user: userData } = response?.data || response;
    localStorage.setItem('careeros_jwt', jwtToken);
    localStorage.setItem('careeros_user', JSON.stringify(userData));
    setToken(jwtToken);
    setUser(userData);
    return userData;
  }, []);

  // ─── Register ────────────────────────────────────────────────────────────
  const register = useCallback(async (data) => {
    const response = await authService.register(data);
    const { token: jwtToken, user: userData } = response?.data || response;
    localStorage.setItem('careeros_jwt', jwtToken);
    localStorage.setItem('careeros_user', JSON.stringify(userData));
    setToken(jwtToken);
    setUser(userData);
    return userData;
  }, []);

  // ─── Logout ──────────────────────────────────────────────────────────────
  const logout = useCallback(async () => {
    try {
      await authService.logout();
    } catch {
      // Ignore errors — still clear local session
    } finally {
      localStorage.removeItem('careeros_jwt');
      localStorage.removeItem('careeros_user');
      setToken(null);
      setUser(null);
    }
  }, []);

  // ─── Fallback login (for dev/mock mode) ──────────────────────────────────
  const loginMock = useCallback((jwtToken, userPayload) => {
    localStorage.setItem('careeros_jwt', jwtToken);
    localStorage.setItem('careeros_user', JSON.stringify(userPayload));
    setToken(jwtToken);
    setUser(userPayload);
  }, []);

  const toggleTheme = () => setTheme(prev => (prev === 'dark' ? 'light' : 'dark'));

  const isAuthenticated = !!token && !!user;

  return (
    <AuthContext.Provider value={{
      user, token, loading, isAuthenticated,
      login, register, logout, loginMock,
      theme, toggleTheme
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};
