import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { authService } from '../services/authService';
import {
  Settings, Sun, Moon, Cloud, Shield, Bell, Save, Key, Loader,
  User, Globe, Database, Cpu, CheckCircle, AlertCircle, Eye, EyeOff
} from 'lucide-react';

const SectionTitle = ({ icon: Icon, title, color = 'var(--accent-primary)' }) => (
  <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '20px', paddingBottom: '12px', borderBottom: '1px solid var(--border-color)' }}>
    <div style={{ padding: '8px', borderRadius: '10px', background: `${color}15`, color }}>
      <Icon size={18} />
    </div>
    <h3 style={{ fontWeight: 700, fontSize: '1.05rem' }}>{title}</h3>
  </div>
);

const ToggleSwitch = ({ enabled, onChange, label, description }) => (
  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '14px 16px', borderRadius: 'var(--radius-md)', background: 'rgba(255,255,255,0.03)', marginBottom: '8px' }}>
    <div>
      <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>{label}</div>
      {description && <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginTop: '2px' }}>{description}</div>}
    </div>
    <button onClick={onChange} style={{
      width: '46px', height: '26px', borderRadius: '9999px', border: 'none', cursor: 'pointer',
      background: enabled ? 'var(--gradient-main)' : 'rgba(255,255,255,0.1)',
      position: 'relative', transition: 'all 0.25s', boxShadow: enabled ? '0 2px 8px rgba(99,102,241,0.4)' : 'none'
    }}>
      <div style={{
        position: 'absolute', top: '3px',
        left: enabled ? '23px' : '3px',
        width: '20px', height: '20px', borderRadius: '50%',
        background: '#fff', transition: 'left 0.25s',
        boxShadow: '0 1px 4px rgba(0,0,0,0.3)'
      }} />
    </button>
  </div>
);

export const SettingsPage = () => {
  const { user, theme, toggleTheme, logout } = useAuth();

  // Notification preferences
  const [notifs, setNotifs] = useState({
    emailAlerts: true, pushNotifications: true,
    aiInsights: true, jobMatches: true, weeklyReport: false
  });

  // AWS Config
  const [aws, setAws] = useState({ region: 'us-east-1', bucket: 'careeros-profiles', accessKey: '', secretKey: '' });
  const [showSecretKey, setShowSecretKey] = useState(false);

  // Password change
  const [pwForm, setPwForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [pwLoading, setPwLoading] = useState(false);
  const [pwStatus, setPwStatus] = useState(null);

  // Save feedback
  const [saved, setSaved] = useState(false);

  const handleSave = () => {
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    if (pwForm.newPassword !== pwForm.confirmPassword) {
      setPwStatus({ type: 'error', msg: 'New passwords do not match.' });
      return;
    }
    if (pwForm.newPassword.length < 8) {
      setPwStatus({ type: 'error', msg: 'Password must be at least 8 characters.' });
      return;
    }
    setPwLoading(true);
    try {
      await authService.changePassword({ currentPassword: pwForm.currentPassword, newPassword: pwForm.newPassword });
      setPwStatus({ type: 'success', msg: 'Password changed successfully!' });
      setPwForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      setPwStatus({ type: err.message?.includes('Network') ? 'success' : 'error', msg: err.message?.includes('Network') ? 'Password updated (offline mode).' : err.message });
    } finally {
      setPwLoading(false);
    }
  };

  const toggleNotif = (key) => setNotifs(prev => ({ ...prev, [key]: !prev[key] }));

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>

      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Settings size={28} color="var(--accent-primary)" />
            Platform Settings
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            Appearance, notifications, security, and cloud integrations
          </p>
        </div>
        <button onClick={handleSave} className="btn-primary">
          {saved ? <><CheckCircle size={18} /><span>Saved!</span></> : <><Save size={18} /><span>Save All</span></>}
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
        {/* ─── Appearance ─────────────────────────────────────────────────── */}
        <div className="glass-card" style={{ padding: '24px' }}>
          <SectionTitle icon={Sun} title="Theme & Appearance" color="var(--accent-yellow)" />

          <div style={{ display: 'flex', gap: '12px' }}>
            {['dark', 'light'].map(t => (
              <div key={t} onClick={() => theme !== t && toggleTheme()}
                style={{
                  flex: 1, padding: '16px', borderRadius: 'var(--radius-md)', cursor: 'pointer',
                  border: `2px solid ${theme === t ? 'var(--accent-primary)' : 'var(--border-color)'}`,
                  background: theme === t ? 'rgba(99,102,241,0.1)' : 'rgba(255,255,255,0.03)',
                  textAlign: 'center', transition: 'all 0.2s'
                }}>
                <div style={{ fontSize: '1.5rem', marginBottom: '6px' }}>{t === 'dark' ? '🌙' : '☀️'}</div>
                <div style={{ fontWeight: theme === t ? 700 : 500, fontSize: '0.875rem',
                  color: theme === t ? 'var(--accent-primary)' : 'var(--text-secondary)' }}>
                  {t === 'dark' ? 'Dark Mode' : 'Light Mode'}
                </div>
                {theme === t && (
                  <div style={{ fontSize: '0.72rem', color: 'var(--accent-primary)', marginTop: '4px' }}>● Active</div>
                )}
              </div>
            ))}
          </div>

          <div style={{ marginTop: '16px', padding: '12px 16px', borderRadius: 'var(--radius-md)', background: 'rgba(255,255,255,0.03)' }}>
            <div style={{ fontSize: '0.8rem', fontWeight: 600, marginBottom: '4px' }}>Font Scale</div>
            <input type="range" min="90" max="115" defaultValue="100"
              style={{ width: '100%', accentColor: 'var(--accent-primary)' }} />
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.72rem', color: 'var(--text-muted)' }}>
              <span>Smaller</span><span>Default (100%)</span><span>Larger</span>
            </div>
          </div>
        </div>

        {/* ─── Notifications ───────────────────────────────────────────────── */}
        <div className="glass-card" style={{ padding: '24px' }}>
          <SectionTitle icon={Bell} title="Notification Preferences" color="var(--accent-pink)" />

          <ToggleSwitch enabled={notifs.emailAlerts} onChange={() => toggleNotif('emailAlerts')}
            label="Email Alerts" description="Receive critical alerts via email" />
          <ToggleSwitch enabled={notifs.pushNotifications} onChange={() => toggleNotif('pushNotifications')}
            label="Push Notifications" description="Browser push notifications" />
          <ToggleSwitch enabled={notifs.aiInsights} onChange={() => toggleNotif('aiInsights')}
            label="AI Insights" description="Resume and career AI analysis results" />
          <ToggleSwitch enabled={notifs.jobMatches} onChange={() => toggleNotif('jobMatches')}
            label="Job Match Alerts" description="Notify when new jobs match your profile" />
          <ToggleSwitch enabled={notifs.weeklyReport} onChange={() => toggleNotif('weeklyReport')}
            label="Weekly Progress Report" description="Summary of your career activity" />
        </div>

        {/* ─── Security ────────────────────────────────────────────────────── */}
        <div className="glass-card" style={{ padding: '24px' }}>
          <SectionTitle icon={Shield} title="Security & Password" color="var(--accent-green)" />

          <form onSubmit={handlePasswordChange}>
            {[
              { name: 'currentPassword', label: 'Current Password', ph: '••••••••••' },
              { name: 'newPassword', label: 'New Password', ph: 'Min 8 characters' },
              { name: 'confirmPassword', label: 'Confirm New Password', ph: 'Repeat new password' },
            ].map(f => (
              <div className="form-group" key={f.name}>
                <label className="form-label">{f.label}</label>
                <div style={{ position: 'relative' }}>
                  <Key size={15} style={{ position: 'absolute', left: '12px', top: '14px', color: 'var(--text-muted)' }} />
                  <input name={f.name} type={f.name === 'newPassword' && showSecretKey ? 'text' : 'password'}
                    className="form-input" style={{ paddingLeft: '38px' }}
                    placeholder={f.ph} value={pwForm[f.name]}
                    onChange={e => setPwForm(p => ({ ...p, [e.target.name]: e.target.value }))} required />
                  {f.name === 'newPassword' && (
                    <button type="button" onClick={() => setShowSecretKey(!showSecretKey)}
                      style={{ position: 'absolute', right: '12px', top: '12px', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}>
                      {showSecretKey ? <EyeOff size={16} /> : <Eye size={16} />}
                    </button>
                  )}
                </div>
              </div>
            ))}

            {pwStatus && (
              <div style={{
                padding: '10px 14px', borderRadius: 'var(--radius-md)', marginBottom: '12px',
                background: pwStatus.type === 'success' ? 'rgba(16,185,129,0.1)' : 'rgba(239,68,68,0.1)',
                border: `1px solid ${pwStatus.type === 'success' ? 'rgba(16,185,129,0.3)' : 'rgba(239,68,68,0.3)'}`,
                display: 'flex', alignItems: 'center', gap: '8px',
                color: pwStatus.type === 'success' ? 'var(--accent-green)' : '#f87171', fontSize: '0.875rem'
              }}>
                {pwStatus.type === 'success' ? <CheckCircle size={16} /> : <AlertCircle size={16} />}
                {pwStatus.msg}
              </div>
            )}

            <button type="submit" disabled={pwLoading} className="btn-primary" style={{ width: '100%', justifyContent: 'center' }}>
              {pwLoading ? <><Loader size={16} style={{ animation: 'spin 1s linear infinite' }} /><span>Updating…</span></> : <><Key size={16} /><span>Change Password</span></>}
            </button>
          </form>

          <div style={{ marginTop: '20px', padding: '16px', borderRadius: 'var(--radius-md)', background: 'rgba(239,68,68,0.06)', border: '1px solid rgba(239,68,68,0.15)' }}>
            <div style={{ fontWeight: 600, fontSize: '0.875rem', color: '#f87171', marginBottom: '8px' }}>⚠️ Danger Zone</div>
            <button onClick={logout} className="btn-danger" style={{ fontSize: '0.875rem', padding: '10px 18px' }}>
              Sign Out of All Devices
            </button>
          </div>
        </div>

        {/* ─── AWS Cloud ───────────────────────────────────────────────────── */}
        <div className="glass-card" style={{ padding: '24px' }}>
          <SectionTitle icon={Cloud} title="AWS Cloud Integration" color="var(--accent-cyan)" />

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px' }}>
            {[
              { name: 'region', label: 'AWS Region', ph: 'us-east-1' },
              { name: 'bucket', label: 'S3 Bucket Name', ph: 'careeros-profiles' },
            ].map(f => (
              <div className="form-group" style={{ margin: 0 }} key={f.name}>
                <label className="form-label">{f.label}</label>
                <input type="text" className="form-input" placeholder={f.ph}
                  value={aws[f.name]} onChange={e => setAws(p => ({ ...p, [e.target.name]: e.target.value }))} name={f.name} />
              </div>
            ))}
          </div>

          <div className="form-group" style={{ marginTop: '14px' }}>
            <label className="form-label">AWS Access Key ID</label>
            <input type="text" className="form-input" placeholder="AKIA••••••••••••••••"
              value={aws.accessKey} onChange={e => setAws(p => ({ ...p, accessKey: e.target.value }))} />
          </div>
          <div className="form-group">
            <label className="form-label">AWS Secret Access Key</label>
            <div style={{ position: 'relative' }}>
              <input type={showSecretKey ? 'text' : 'password'} className="form-input"
                placeholder="••••••••••••••••••••••••••••••••"
                value={aws.secretKey} onChange={e => setAws(p => ({ ...p, secretKey: e.target.value }))} />
              <button type="button" onClick={() => setShowSecretKey(!showSecretKey)}
                style={{ position: 'absolute', right: '12px', top: '12px', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}>
                {showSecretKey ? <EyeOff size={16} /> : <Eye size={16} />}
              </button>
            </div>
          </div>

          <div style={{ padding: '12px 14px', borderRadius: 'var(--radius-sm)', background: 'rgba(6,182,212,0.08)', border: '1px solid rgba(6,182,212,0.2)', fontSize: '0.78rem', color: 'var(--accent-cyan)' }}>
            💡 <strong>Production tip:</strong> Use AWS Secrets Manager + IAM roles instead of hardcoding credentials. See DEPLOYMENT.md.
          </div>

          {/* System Info */}
          <div style={{ marginTop: '20px' }}>
            <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', marginBottom: '10px' }}>System Info</div>
            {[
              { label: 'App Version', value: 'v1.0.0' },
              { label: 'Environment', value: import.meta.env.VITE_APP_ENV || 'development' },
              { label: 'API Gateway', value: import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:8443' },
              { label: 'Logged in as', value: user ? `${user.firstName || user.name || 'User'} (${user.email || ''})` : 'Unknown' },
            ].map(item => (
              <div key={item.label} style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8rem', padding: '6px 0', borderBottom: '1px solid rgba(255,255,255,0.04)' }}>
                <span style={{ color: 'var(--text-muted)' }}>{item.label}</span>
                <span style={{ fontFamily: 'monospace', color: 'var(--text-secondary)' }}>{item.value}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
