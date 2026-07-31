import React, { useState, useEffect } from 'react';
import { jobService } from '../services/jobService';
import { Briefcase, Plus, Trash2, ChevronRight, ExternalLink, Loader, Calendar, Building2 } from 'lucide-react';

// ─── Kanban columns with status mapping ──────────────────────────────────────
const COLUMNS = [
  { id: 'WISHLIST',    label: '⭐ Wishlist',    color: 'var(--accent-cyan)',    bg: 'rgba(6,182,212,0.08)' },
  { id: 'APPLIED',     label: '📨 Applied',     color: 'var(--accent-primary)', bg: 'rgba(99,102,241,0.08)' },
  { id: 'INTERVIEWING',label: '🎤 Interviewing',color: 'var(--accent-yellow)',  bg: 'rgba(245,158,11,0.08)' },
  { id: 'OFFERED',     label: '🎉 Offered',     color: 'var(--accent-green)',   bg: 'rgba(16,185,129,0.08)' },
  { id: 'REJECTED',    label: '❌ Rejected',    color: 'var(--accent-red)',     bg: 'rgba(239,68,68,0.08)' },
];

// ─── Mock seed data for offline/dev mode ─────────────────────────────────────
const MOCK_APPLICATIONS = [
  { id: '1', status: 'APPLIED',     company: 'Google',    title: 'Staff Software Engineer',  appliedDate: '2024-07-20', salary: '$180K–$240K', jobUrl: '#' },
  { id: '2', status: 'INTERVIEWING',company: 'Stripe',    title: 'Senior Backend Engineer',  appliedDate: '2024-07-18', salary: '$160K–$200K', jobUrl: '#' },
  { id: '3', status: 'WISHLIST',    company: 'Figma',     title: 'Principal Engineer',       appliedDate: null,         salary: '$200K+',       jobUrl: '#' },
  { id: '4', status: 'OFFERED',     company: 'Vercel',    title: 'Lead Platform Engineer',   appliedDate: '2024-07-10', salary: '$175K',        jobUrl: '#' },
  { id: '5', status: 'APPLIED',     company: 'Shopify',   title: 'Senior Engineer II',       appliedDate: '2024-07-22', salary: '$155K–$185K', jobUrl: '#' },
  { id: '6', status: 'REJECTED',    company: 'Meta',      title: 'E6 Software Engineer',     appliedDate: '2024-07-05', salary: '$210K',        jobUrl: '#' },
];

const JobCard = ({ app, onStatusChange, onDelete, isUpdating }) => (
  <div className="glass-card" style={{
    padding: '16px', borderRadius: 'var(--radius-md)', cursor: 'grab',
    transition: 'all 0.2s ease', border: '1px solid var(--border-color)',
    opacity: isUpdating ? 0.6 : 1
  }}>
    {/* Company + Title */}
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '10px' }}>
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
          <Building2 size={14} color="var(--text-muted)" />
          <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontWeight: 500 }}>{app.company}</span>
        </div>
        <h4 style={{ fontSize: '0.95rem', fontWeight: 700, lineHeight: 1.3 }}>{app.title}</h4>
      </div>
      {app.jobUrl && app.jobUrl !== '#' && (
        <a href={app.jobUrl} target="_blank" rel="noreferrer" style={{ color: 'var(--text-muted)' }}>
          <ExternalLink size={14} />
        </a>
      )}
    </div>

    {/* Salary + Date */}
    <div style={{ display: 'flex', gap: '8px', marginBottom: '12px', flexWrap: 'wrap' }}>
      {app.salary && (
        <span className="badge badge-green" style={{ fontSize: '0.7rem' }}>{app.salary}</span>
      )}
      {app.appliedDate && (
        <span style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.72rem', color: 'var(--text-muted)' }}>
          <Calendar size={11} />
          {new Date(app.appliedDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}
        </span>
      )}
    </div>

    {/* Move + Delete actions */}
    <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
      {COLUMNS.filter(c => c.id !== app.status).map(col => (
        <button key={col.id} onClick={() => onStatusChange(app.id, col.id)}
          disabled={isUpdating}
          style={{
            padding: '4px 8px', fontSize: '0.7rem', borderRadius: '6px', cursor: 'pointer',
            background: col.bg, border: `1px solid ${col.color}30`, color: col.color,
            fontWeight: 500, transition: 'all 0.15s'
          }}>
          → {col.label.split(' ')[1]}
        </button>
      ))}
      <button onClick={() => onDelete(app.id)} disabled={isUpdating}
        style={{ marginLeft: 'auto', padding: '4px 8px', borderRadius: '6px', cursor: 'pointer',
          background: 'rgba(239,68,68,0.1)', border: '1px solid rgba(239,68,68,0.2)', color: '#f87171' }}>
        <Trash2 size={12} />
      </button>
    </div>
  </div>
);

const AddJobModal = ({ onClose, onAdd }) => {
  const [form, setForm] = useState({ company: '', title: '', salary: '', jobUrl: '', notes: '' });
  const [saving, setSaving] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    await onAdd({ ...form, status: 'WISHLIST', appliedDate: null });
    setSaving(false);
    onClose();
  };

  return (
    <div style={{
      position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(4px)',
      display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
    }}>
      <div className="glass-card" style={{ width: '460px', padding: '32px', borderRadius: 'var(--radius-xl)' }}>
        <h2 style={{ fontWeight: 800, marginBottom: '24px' }}>Add Job Application</h2>
        <form onSubmit={handleSubmit}>
          {[
            { name: 'company', label: 'Company *', type: 'text', placeholder: 'e.g. Google', required: true },
            { name: 'title', label: 'Job Title *', type: 'text', placeholder: 'e.g. Senior Engineer', required: true },
            { name: 'salary', label: 'Salary Range', type: 'text', placeholder: 'e.g. $150K–$180K' },
            { name: 'jobUrl', label: 'Job URL', type: 'url', placeholder: 'https://...' },
          ].map(field => (
            <div className="form-group" key={field.name}>
              <label className="form-label">{field.label}</label>
              <input name={field.name} type={field.type} className="form-input"
                placeholder={field.placeholder} required={field.required}
                value={form[field.name]}
                onChange={e => setForm(p => ({ ...p, [e.target.name]: e.target.value }))} />
            </div>
          ))}
          <div className="form-group">
            <label className="form-label">Notes</label>
            <textarea name="notes" className="form-input" rows={2} placeholder="Any notes about this role..."
              value={form.notes} onChange={e => setForm(p => ({ ...p, notes: e.target.value }))} />
          </div>
          <div style={{ display: 'flex', gap: '12px', marginTop: '8px' }}>
            <button type="button" onClick={onClose} className="btn-secondary" style={{ flex: 1, justifyContent: 'center' }}>Cancel</button>
            <button type="submit" disabled={saving} className="btn-primary" style={{ flex: 1, justifyContent: 'center' }}>
              {saving ? <Loader size={16} style={{ animation: 'spin 1s linear infinite' }} /> : <><Plus size={16} /><span>Add Job</span></>}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export const JobTrackerPage = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await jobService.getMyApplications();
        setApplications(Array.isArray(data) ? data : data?.content || []);
      } catch {
        setApplications(MOCK_APPLICATIONS); // offline fallback
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleStatusChange = async (appId, newStatus) => {
    setUpdatingId(appId);
    setApplications(prev => prev.map(a => a.id === appId ? { ...a, status: newStatus } : a));
    try {
      await jobService.updateApplicationStatus(appId, newStatus);
    } catch {
      // Keep optimistic update even if offline
    } finally {
      setUpdatingId(null);
    }
  };

  const handleDelete = async (appId) => {
    setApplications(prev => prev.filter(a => a.id !== appId));
    try { await jobService.deleteApplication(appId); } catch { /* offline */ }
  };

  const handleAdd = async (jobData) => {
    const newApp = { ...jobData, id: `local-${Date.now()}` };
    setApplications(prev => [...prev, newApp]);
    try {
      const created = await jobService.applyToJob(null, jobData);
      if (created?.id) {
        setApplications(prev => prev.map(a => a.id === newApp.id ? created : a));
      }
    } catch { /* keep local */ }
  };

  const stats = COLUMNS.map(col => ({
    ...col, count: applications.filter(a => a.status === col.id).length
  }));

  if (loading) return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '60vh' }}>
      <Loader size={40} style={{ animation: 'spin 1s linear infinite', color: 'var(--accent-primary)' }} />
    </div>
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>

      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Briefcase size={28} color="var(--accent-primary)" />
            Job Application Tracker
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            {applications.length} total applications tracked
          </p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary">
          <Plus size={18} /><span>Add Application</span>
        </button>
      </div>

      {/* Stats row */}
      <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
        {stats.map(s => (
          <div key={s.id} style={{
            padding: '10px 18px', borderRadius: 'var(--radius-md)',
            background: s.bg, border: `1px solid ${s.color}30`,
            display: 'flex', alignItems: 'center', gap: '8px'
          }}>
            <span style={{ color: s.color, fontWeight: 800, fontSize: '1.2rem' }}>{s.count}</span>
            <span style={{ color: s.color, fontSize: '0.8rem', fontWeight: 600 }}>{s.label}</span>
          </div>
        ))}
      </div>

      {/* Kanban Board */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(5, 1fr)', gap: '16px', alignItems: 'start' }}>
        {COLUMNS.map(col => {
          const colApps = applications.filter(a => a.status === col.id);
          return (
            <div key={col.id} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {/* Column Header */}
              <div style={{
                padding: '10px 14px', borderRadius: 'var(--radius-md)',
                background: col.bg, border: `1px solid ${col.color}40`,
                display: 'flex', justifyContent: 'space-between', alignItems: 'center'
              }}>
                <span style={{ color: col.color, fontWeight: 700, fontSize: '0.875rem' }}>{col.label}</span>
                <span style={{
                  background: col.color, color: '#fff', borderRadius: '50%',
                  width: '22px', height: '22px', display: 'flex', alignItems: 'center',
                  justifyContent: 'center', fontSize: '0.75rem', fontWeight: 700
                }}>{colApps.length}</span>
              </div>

              {/* Cards */}
              {colApps.map(app => (
                <JobCard key={app.id} app={app}
                  onStatusChange={handleStatusChange}
                  onDelete={handleDelete}
                  isUpdating={updatingId === app.id} />
              ))}

              {colApps.length === 0 && (
                <div style={{
                  padding: '24px', textAlign: 'center', color: 'var(--text-muted)',
                  fontSize: '0.8rem', border: '1px dashed var(--border-color)',
                  borderRadius: 'var(--radius-md)'
                }}>
                  No applications
                </div>
              )}
            </div>
          );
        })}
      </div>

      {showModal && <AddJobModal onClose={() => setShowModal(false)} onAdd={handleAdd} />}
    </div>
  );
};
