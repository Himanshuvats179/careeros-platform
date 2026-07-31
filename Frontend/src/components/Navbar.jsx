import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { Sun, Moon, Bell, Search, ShieldCheck, LogOut } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { notificationService } from '../services/auditService';

export const Navbar = () => {
  const { user, theme, toggleTheme, logout } = useAuth();
  const navigate = useNavigate();
  const [unreadCount, setUnreadCount] = useState(0);
  const [showUserMenu, setShowUserMenu] = useState(false);

  // Poll unread notification count
  useEffect(() => {
    const fetchCount = async () => {
      try {
        const res = await notificationService.getUnreadCount();
        setUnreadCount(res?.count ?? res?.data ?? 3);
      } catch {
        setUnreadCount(3); // demo badge
      }
    };
    fetchCount();
    const iv = setInterval(fetchCount, 60000);
    return () => clearInterval(iv);
  }, []);

  const displayName = user?.firstName
    ? `${user.firstName} ${user.lastName || ''}`.trim()
    : user?.name || 'User';
  const displayInitial = displayName[0]?.toUpperCase() || 'U';
  const displayRole = user?.role || user?.title || 'CareerOS Member';

  const handleLogout = async () => {
    await logout();
    navigate('/auth');
  };

  return (
    <header className="glass-nav" style={{
      position: 'fixed', top: 0, right: 0, left: 0, height: '70px',
      zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      padding: '0 32px'
    }}>
      {/* Logo */}
      <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '10px' }}>
        <div style={{
          width: '38px', height: '38px', borderRadius: '10px', background: 'var(--gradient-main)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          boxShadow: '0 4px 14px rgba(99,102,241,0.4)'
        }}>
          <ShieldCheck size={20} color="#fff" />
        </div>
        <span style={{ fontSize: '1.25rem', fontWeight: 800, color: 'var(--text-primary)', fontFamily: 'var(--font-heading)' }}>
          Career<span style={{ color: 'var(--accent-primary)' }}>OS</span>
        </span>
      </Link>

      {/* Search */}
      <div style={{ display: 'flex', alignItems: 'center', width: '36%', position: 'relative' }}>
        <Search size={17} style={{ position: 'absolute', left: '14px', color: 'var(--text-muted)', pointerEvents: 'none' }} />
        <input type="text" placeholder="Search jobs, skills, audit logs…" className="form-input"
          style={{ paddingLeft: '42px', borderRadius: '9999px', background: 'rgba(255,255,255,0.05)' }} />
      </div>

      {/* Right controls */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
        {/* Theme toggle */}
        <button onClick={toggleTheme} className="btn-secondary"
          style={{ borderRadius: '50%', width: '40px', height: '40px', padding: 0, justifyContent: 'center' }}>
          {theme === 'dark'
            ? <Sun size={18} color="#f59e0b" />
            : <Moon size={18} color="#6366f1" />}
        </button>

        {/* Notifications bell */}
        <Link to="/notifications" className="btn-secondary"
          style={{ borderRadius: '50%', width: '40px', height: '40px', padding: 0, justifyContent: 'center', position: 'relative', textDecoration: 'none', display: 'flex', alignItems: 'center' }}>
          <Bell size={18} />
          {unreadCount > 0 && (
            <span style={{
              position: 'absolute', top: '4px', right: '4px',
              background: 'var(--accent-pink)', color: '#fff',
              borderRadius: '50%', width: '16px', height: '16px',
              fontSize: '0.6rem', fontWeight: 800,
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              border: '1.5px solid var(--bg-primary)'
            }}>{unreadCount > 9 ? '9+' : unreadCount}</span>
          )}
        </Link>

        {/* User menu */}
        {user && (
          <div style={{ position: 'relative' }}>
            <button onClick={() => setShowUserMenu(!showUserMenu)}
              style={{
                display: 'flex', alignItems: 'center', gap: '10px', padding: '6px 12px',
                borderRadius: 'var(--radius-md)', background: 'rgba(255,255,255,0.05)',
                border: '1px solid var(--border-color)', cursor: 'pointer', textDecoration: 'none'
              }}>
              <div style={{
                width: '34px', height: '34px', borderRadius: '50%',
                background: 'var(--gradient-main)', display: 'flex', alignItems: 'center',
                justifyContent: 'center', color: '#fff', fontWeight: 700, fontSize: '0.95rem',
                boxShadow: '0 2px 8px rgba(99,102,241,0.4)'
              }}>{displayInitial}</div>
              <div style={{ display: 'flex', flexDirection: 'column', textAlign: 'left' }}>
                <span style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-primary)' }}>{displayName}</span>
                <span style={{ fontSize: '0.72rem', color: 'var(--text-secondary)' }}>{displayRole}</span>
              </div>
            </button>

            {/* Dropdown */}
            {showUserMenu && (
              <div style={{
                position: 'absolute', top: 'calc(100% + 8px)', right: 0, minWidth: '180px',
                background: 'var(--bg-card)', border: '1px solid var(--border-color)',
                borderRadius: 'var(--radius-lg)', padding: '8px',
                boxShadow: '0 8px 32px rgba(0,0,0,0.4)', backdropFilter: 'blur(20px)', zIndex: 200
              }}>
                <Link to="/profile" onClick={() => setShowUserMenu(false)} style={{
                  display: 'flex', alignItems: 'center', gap: '10px', padding: '10px 12px',
                  borderRadius: 'var(--radius-md)', color: 'var(--text-primary)', textDecoration: 'none',
                  fontSize: '0.875rem', fontWeight: 500
                }}>👤 My Profile</Link>
                <Link to="/settings" onClick={() => setShowUserMenu(false)} style={{
                  display: 'flex', alignItems: 'center', gap: '10px', padding: '10px 12px',
                  borderRadius: 'var(--radius-md)', color: 'var(--text-primary)', textDecoration: 'none',
                  fontSize: '0.875rem', fontWeight: 500
                }}>⚙️ Settings</Link>
                <div style={{ height: '1px', background: 'var(--border-color)', margin: '6px 0' }} />
                <button onClick={handleLogout} style={{
                  width: '100%', display: 'flex', alignItems: 'center', gap: '10px', padding: '10px 12px',
                  borderRadius: 'var(--radius-md)', border: 'none', background: 'none', cursor: 'pointer',
                  color: '#f87171', fontSize: '0.875rem', fontWeight: 500, textAlign: 'left'
                }}>
                  <LogOut size={15} />Sign Out
                </button>
              </div>
            )}
          </div>
        )}
      </div>

      {/* Close user menu on outside click */}
      {showUserMenu && (
        <div onClick={() => setShowUserMenu(false)}
          style={{ position: 'fixed', inset: 0, zIndex: 150 }} />
      )}
    </header>
  );
};
