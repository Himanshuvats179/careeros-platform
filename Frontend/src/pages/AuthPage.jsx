import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { ShieldCheck, Mail, Lock, User, ArrowRight, Loader, AlertCircle } from 'lucide-react';

export const AuthPage = () => {
  const [isRegister, setIsRegister] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: 'demo@careeros.com', password: 'Password123!'
  });
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const { login, register, loginMock } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setError(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      if (isRegister) {
        await register(formData);
      } else {
        await login({ email: formData.email, password: formData.password });
      }
      navigate('/dashboard');
    } catch (err) {
      // If backend unavailable, fall through to mock mode for development
      if (err.message?.includes('Network Error') || err.message?.includes('ECONNREFUSED')) {
        loginMock('dev-mock-jwt-token-careeros', {
          id: 'dev-user-001', firstName: 'Alex', lastName: 'Rivera',
          email: formData.email, role: 'Senior Software Architect'
        });
        navigate('/dashboard');
      } else {
        setError(err.message || 'Authentication failed. Please try again.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center',
      background: 'radial-gradient(ellipse at top right, rgba(99, 102, 241, 0.18) 0%, transparent 50%), radial-gradient(ellipse at bottom left, rgba(236, 72, 153, 0.12) 0%, transparent 50%), var(--bg-primary)'
    }}>
      {/* Animated background blobs */}
      <div style={{
        position: 'fixed', width: '500px', height: '500px', borderRadius: '50%',
        background: 'rgba(99,102,241,0.06)', filter: 'blur(80px)',
        top: '-100px', right: '-100px', pointerEvents: 'none'
      }} />

      <div className="glass-card" style={{ width: '440px', padding: '44px', borderRadius: 'var(--radius-xl)' }}>
        {/* Logo */}
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{
            width: '56px', height: '56px', borderRadius: '16px',
            background: 'var(--gradient-main)', display: 'inline-flex',
            alignItems: 'center', justifyContent: 'center', marginBottom: '16px',
            boxShadow: '0 8px 24px rgba(99,102,241,0.4)'
          }}>
            <ShieldCheck size={28} color="#fff" />
          </div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, marginBottom: '6px' }}>
            {isRegister ? 'Join CareerOS' : 'Welcome Back'}
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
            {isRegister
              ? 'Build your AI-powered career journey'
              : 'Sign in to your career dashboard'}
          </p>
        </div>

        {/* Error Banner */}
        {error && (
          <div style={{
            padding: '12px 16px', borderRadius: 'var(--radius-md)', marginBottom: '20px',
            background: 'rgba(239,68,68,0.12)', border: '1px solid rgba(239,68,68,0.3)',
            display: 'flex', alignItems: 'center', gap: '10px', color: '#f87171'
          }}>
            <AlertCircle size={18} />
            <span style={{ fontSize: '0.875rem' }}>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          {isRegister && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '16px' }}>
              <div className="form-group" style={{ margin: 0 }}>
                <label className="form-label">First Name</label>
                <div style={{ position: 'relative' }}>
                  <User size={16} style={{ position: 'absolute', left: '12px', top: '14px', color: 'var(--text-muted)' }} />
                  <input name="firstName" type="text" className="form-input" style={{ paddingLeft: '38px' }}
                    value={formData.firstName} onChange={handleChange} required placeholder="Alex" />
                </div>
              </div>
              <div className="form-group" style={{ margin: 0 }}>
                <label className="form-label">Last Name</label>
                <div style={{ position: 'relative' }}>
                  <User size={16} style={{ position: 'absolute', left: '12px', top: '14px', color: 'var(--text-muted)' }} />
                  <input name="lastName" type="text" className="form-input" style={{ paddingLeft: '38px' }}
                    value={formData.lastName} onChange={handleChange} required placeholder="Rivera" />
                </div>
              </div>
            </div>
          )}

          <div className="form-group">
            <label className="form-label">Email Address</label>
            <div style={{ position: 'relative' }}>
              <Mail size={16} style={{ position: 'absolute', left: '12px', top: '14px', color: 'var(--text-muted)' }} />
              <input name="email" type="email" className="form-input" style={{ paddingLeft: '38px' }}
                value={formData.email} onChange={handleChange} required placeholder="you@company.com" />
            </div>
          </div>

          <div className="form-group" style={{ marginBottom: '28px' }}>
            <label className="form-label">Password</label>
            <div style={{ position: 'relative' }}>
              <Lock size={16} style={{ position: 'absolute', left: '12px', top: '14px', color: 'var(--text-muted)' }} />
              <input name="password" type="password" className="form-input" style={{ paddingLeft: '38px' }}
                value={formData.password} onChange={handleChange} required placeholder="••••••••••••" />
            </div>
            {!isRegister && (
              <div style={{ textAlign: 'right', marginTop: '6px' }}>
                <button type="button" style={{ background: 'none', border: 'none', color: 'var(--accent-primary)', fontSize: '0.8rem', cursor: 'pointer' }}>
                  Forgot password?
                </button>
              </div>
            )}
          </div>

          <button type="submit" disabled={isLoading} className="btn-primary"
            style={{ width: '100%', justifyContent: 'center', padding: '14px', fontSize: '1rem' }}>
            {isLoading
              ? <><Loader size={18} style={{ animation: 'spin 1s linear infinite' }} /><span>Authenticating…</span></>
              : <><span>{isRegister ? 'Create Account' : 'Sign In'}</span><ArrowRight size={18} /></>
            }
          </button>
        </form>

        {/* Divider */}
        <div style={{ textAlign: 'center', margin: '24px 0 0', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
          {isRegister ? 'Already have an account?' : "Don't have an account?"}{' '}
          <button onClick={() => { setIsRegister(!isRegister); setError(null); }}
            style={{ background: 'none', border: 'none', color: 'var(--accent-primary)', fontWeight: 600, cursor: 'pointer' }}>
            {isRegister ? 'Sign In' : 'Register Now'}
          </button>
        </div>

        {/* Dev hint */}
        <div style={{ marginTop: '16px', padding: '10px 14px', borderRadius: 'var(--radius-sm)', background: 'rgba(99,102,241,0.08)', border: '1px solid rgba(99,102,241,0.2)', textAlign: 'center' }}>
          <span style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>
            💡 <strong>Dev mode:</strong> If backend is offline, demo login auto-activates.
          </span>
        </div>
      </div>
    </div>
  );
};
