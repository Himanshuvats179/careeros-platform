import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard, User, FileText, Compass, MessageSquare,
  Briefcase, Activity, Settings, HelpCircle, Bot, Sparkles
} from 'lucide-react';

export const Sidebar = () => {
  const navItems = [
    { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { path: '/profile', label: 'My Profile', icon: User },
    { path: '/resume', label: 'Resume & ATS', icon: FileText },
    { path: '/career-roadmap', label: 'Career Roadmap', icon: Compass },
    { path: '/interview', label: 'Mock Interview', icon: Sparkles },
    { path: '/ai-chat', label: 'AI Career Coach', icon: Bot },
    { path: '/job-tracker', label: 'Job Tracker', icon: Briefcase },
    { path: '/audit-logs', label: 'Audit Logs', icon: Activity },
    { path: '/settings', label: 'Settings', icon: Settings },
  ];

  return (
    <aside className="glass-nav" style={{
      width: '260px', position: 'fixed', top: '70px', bottom: 0, left: 0,
      padding: '24px 16px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between'
    }}>
      <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
        <span style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', paddingLeft: '12px', marginBottom: '8px' }}>
          Platform Navigation
        </span>

        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              style={({ isActive }) => ({
                display: 'flex',
                alignItems: 'center',
                gap: '12px',
                padding: '12px 16px',
                borderRadius: 'var(--radius-md)',
                textDecoration: 'none',
                fontWeight: 500,
                fontSize: '0.95rem',
                color: isActive ? '#ffffff' : 'var(--text-secondary)',
                background: isActive ? 'var(--gradient-main)' : 'transparent',
                boxShadow: isActive ? 'var(--shadow-glow)' : 'none',
                transition: 'all 0.2s ease'
              })}
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </div>

      <div className="glass-card" style={{ padding: '16px', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
        <div style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-primary)', marginBottom: '4px' }}>CareerOS AI Pro</div>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginBottom: '12px' }}>FastAPI + LangChain + Kafka</div>
        <button className="btn-primary" style={{ width: '100%', justifyContent: 'center', fontSize: '0.8rem', padding: '6px 12px' }}>
          System Health
        </button>
      </div>
    </aside>
  );
};
