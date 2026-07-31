import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { notificationService } from '../services/auditService';
import {
  Bell, CheckCheck, Trash2, Sparkles, Briefcase, FileText,
  ShieldCheck, Activity, Info, AlertTriangle, CheckCircle, Loader, RefreshCw
} from 'lucide-react';

const TYPE_CONFIG = {
  SUCCESS:  { icon: CheckCircle,   color: 'var(--accent-green)',   bg: 'rgba(16,185,129,0.1)',  border: 'rgba(16,185,129,0.25)' },
  INFO:     { icon: Info,          color: 'var(--accent-cyan)',    bg: 'rgba(6,182,212,0.1)',   border: 'rgba(6,182,212,0.25)'  },
  WARNING:  { icon: AlertTriangle, color: 'var(--accent-yellow)', bg: 'rgba(245,158,11,0.1)',  border: 'rgba(245,158,11,0.25)' },
  AI:       { icon: Sparkles,      color: 'var(--accent-primary)', bg: 'rgba(99,102,241,0.1)', border: 'rgba(99,102,241,0.25)' },
  JOB:      { icon: Briefcase,     color: 'var(--accent-pink)',    bg: 'rgba(236,72,153,0.1)', border: 'rgba(236,72,153,0.25)' },
  SECURITY: { icon: ShieldCheck,   color: 'var(--accent-red)',     bg: 'rgba(239,68,68,0.1)',  border: 'rgba(239,68,68,0.25)'  },
};

const MOCK_NOTIFICATIONS = [
  { id: '1', type: 'AI',       title: 'Resume Analysis Complete',         message: 'GPT-4o analyzed your resume: ATS score 88/100. 5 improvements suggested.', read: false, createdAt: new Date(Date.now() - 5 * 60000).toISOString() },
  { id: '2', type: 'JOB',     title: 'New Job Match Found',               message: 'Staff Engineer at Stripe matches 94% of your profile. Apply now!', read: false, createdAt: new Date(Date.now() - 20 * 60000).toISOString() },
  { id: '3', type: 'AI',      title: 'Interview Evaluation Ready',        message: 'Your System Design answer scored 9/10. Detailed feedback is available.', read: false, createdAt: new Date(Date.now() - 45 * 60000).toISOString() },
  { id: '4', type: 'SUCCESS', title: 'Profile Updated',                   message: 'Your skills and experience sections were saved successfully.', read: true,  createdAt: new Date(Date.now() - 2 * 3600000).toISOString() },
  { id: '5', type: 'INFO',    title: 'Kafka Consumer Healthy',            message: 'Audit service successfully processed 142 events in the last hour.', read: true, createdAt: new Date(Date.now() - 3 * 3600000).toISOString() },
  { id: '6', type: 'WARNING', title: 'Resume Expiring Soon',              message: 'Your uploaded resume will expire in 7 days. Please re-upload to keep it active.', read: true, createdAt: new Date(Date.now() - 5 * 3600000).toISOString() },
  { id: '7', type: 'SECURITY', title: 'New Login Detected',              message: 'New sign-in from Chrome on Windows (192.168.1.42). Was this you?', read: true, createdAt: new Date(Date.now() - 24 * 3600000).toISOString() },
  { id: '8', type: 'JOB',    title: 'Application Status Updated',         message: 'Your application at Google moved to "Interviewing" stage.', read: true, createdAt: new Date(Date.now() - 2 * 86400000).toISOString() },
];

const timeAgo = (iso) => {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return `${m}m ago`;
  const h = Math.floor(m / 60);
  if (h < 24) return `${h}h ago`;
  return `${Math.floor(h / 24)}d ago`;
};

export const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    const load = async () => {
      try {
        const data = await notificationService.getNotifications();
        const items = data?.content || data?.data || data;
        setNotifications(Array.isArray(items) ? items : MOCK_NOTIFICATIONS);
      } catch {
        setNotifications(MOCK_NOTIFICATIONS);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const markAllRead = async () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
    try { await notificationService.markAllAsRead(); } catch { /* offline */ }
  };

  const markRead = async (id) => {
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, read: true } : n));
    try { await notificationService.markAsRead(id); } catch { /* offline */ }
  };

  const deleteNotification = async (id) => {
    setNotifications(prev => prev.filter(n => n.id !== id));
    try { await notificationService.deleteNotification(id); } catch { /* offline */ }
  };

  const filtered = filter === 'unread'
    ? notifications.filter(n => !n.read)
    : notifications;

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Bell size={28} color="var(--accent-primary)" />
            Notifications
            {unreadCount > 0 && (
              <span style={{
                background: 'var(--accent-pink)', color: '#fff', borderRadius: '999px',
                padding: '2px 10px', fontSize: '0.8rem', fontWeight: 700
              }}>{unreadCount}</span>
            )}
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            Real-time updates from CareerOS microservices and AI agent pipelines
          </p>
        </div>
        {unreadCount > 0 && (
          <button onClick={markAllRead} className="btn-secondary">
            <CheckCheck size={16} /><span>Mark All Read</span>
          </button>
        )}
      </div>

      {/* Filter tabs */}
      <div style={{ display: 'flex', gap: '4px', background: 'rgba(255,255,255,0.04)', padding: '4px', borderRadius: 'var(--radius-md)', width: 'fit-content' }}>
        {[{ id: 'all', label: `All (${notifications.length})` }, { id: 'unread', label: `Unread (${unreadCount})` }].map(tab => (
          <button key={tab.id} onClick={() => setFilter(tab.id)}
            style={{
              padding: '8px 20px', borderRadius: 'calc(var(--radius-md) - 2px)', border: 'none', cursor: 'pointer',
              background: filter === tab.id ? 'var(--gradient-main)' : 'transparent',
              color: filter === tab.id ? '#fff' : 'var(--text-secondary)',
              fontWeight: filter === tab.id ? 700 : 500, fontSize: '0.875rem', transition: 'all 0.2s'
            }}>
            {tab.label}
          </button>
        ))}
      </div>

      {/* Notifications list */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px' }}>
          <Loader size={32} style={{ animation: 'spin 1s linear infinite', color: 'var(--accent-primary)' }} />
        </div>
      ) : filtered.length === 0 ? (
        <div className="glass-card" style={{ padding: '60px', textAlign: 'center', color: 'var(--text-muted)' }}>
          <Bell size={48} style={{ opacity: 0.3, marginBottom: '12px' }} />
          <p>No {filter === 'unread' ? 'unread' : ''} notifications</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
          {filtered.map(n => {
            const cfg = TYPE_CONFIG[n.type] || TYPE_CONFIG.INFO;
            const Icon = cfg.icon;
            return (
              <div key={n.id} onClick={() => markRead(n.id)}
                style={{
                  display: 'flex', alignItems: 'flex-start', gap: '16px', padding: '18px 20px',
                  borderRadius: 'var(--radius-lg)', cursor: 'pointer', transition: 'all 0.2s',
                  background: n.read ? 'var(--bg-card)' : cfg.bg,
                  border: `1px solid ${n.read ? 'var(--border-color)' : cfg.border}`,
                  backdropFilter: 'blur(16px)',
                  boxShadow: n.read ? 'none' : `0 4px 16px ${cfg.bg}`
                }}>
                {/* Icon */}
                <div style={{
                  width: '42px', height: '42px', borderRadius: '12px', flexShrink: 0,
                  background: cfg.bg, border: `1px solid ${cfg.border}`,
                  display: 'flex', alignItems: 'center', justifyContent: 'center'
                }}>
                  <Icon size={20} color={cfg.color} />
                </div>

                {/* Content */}
                <div style={{ flex: 1 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '4px' }}>
                    <h3 style={{ fontSize: '0.95rem', fontWeight: n.read ? 600 : 700, color: 'var(--text-primary)' }}>{n.title}</h3>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', flexShrink: 0, marginLeft: '12px' }}>
                      {!n.read && <span style={{ width: '8px', height: '8px', borderRadius: '50%', background: cfg.color, display: 'inline-block' }} />}
                      <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', whiteSpace: 'nowrap' }}>{timeAgo(n.createdAt)}</span>
                    </div>
                  </div>
                  <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: 1.5 }}>{n.message}</p>
                </div>

                {/* Delete */}
                <button onClick={e => { e.stopPropagation(); deleteNotification(n.id); }}
                  style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)', padding: '4px', flexShrink: 0 }}>
                  <Trash2 size={15} />
                </button>
              </div>
            );
          })}
        </div>
      )}
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>
    </div>
  );
};
