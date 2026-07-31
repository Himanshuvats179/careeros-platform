import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Sparkles, FileCheck, Compass, Activity, ArrowUpRight, CheckCircle } from 'lucide-react';
import { Link } from 'react-router-dom';

export const DashboardPage = () => {
  const { user } = useAuth();

  const metrics = [
    { title: 'Profile Completion', value: '92%', icon: FileCheck, color: 'var(--accent-green)', badge: '+8% this week' },
    { title: 'ATS Resume Score', value: '88/100', icon: Sparkles, color: 'var(--accent-primary)', badge: 'Optimal ATS match' },
    { title: 'Roadmap Milestone', value: 'Phase 2', icon: Compass, color: 'var(--accent-pink)', badge: 'On Track' },
    { title: 'AI Recommendations', value: '14 Active', icon: Activity, color: 'var(--accent-cyan)', badge: 'Updated Live' }
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '2rem', fontWeight: 800 }}>Welcome back, {user?.name || 'Alex'} 👋</h1>
          <p style={{ color: 'var(--text-secondary)' }}>CareerOS Executive Dashboard & AI Career Intelligence Platform</p>
        </div>
        <Link to="/ai-chat" className="btn-primary">
          <Sparkles size={18} />
          <span>Ask AI Coach</span>
        </Link>
      </div>

      {/* Metric Cards Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '20px' }}>
        {metrics.map((m, i) => {
          const Icon = m.icon;
          return (
            <div key={i} className="glass-card" style={{ padding: '20px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
                <span style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', fontWeight: 500 }}>{m.title}</span>
                <div style={{ padding: '8px', borderRadius: '10px', background: 'rgba(255, 255, 255, 0.05)', color: m.color }}>
                  <Icon size={20} />
                </div>
              </div>
              <div style={{ fontSize: '1.85rem', fontWeight: 800, marginBottom: '8px' }}>{m.value}</div>
              <span className="badge badge-green">{m.badge}</span>
            </div>
          );
        })}
      </div>

      {/* Quick AI Agent Launch Grid */}
      <h2 style={{ fontSize: '1.35rem', fontWeight: 700, marginTop: '8px' }}>AI Career Agents</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '20px' }}>
        <Link to="/resume" className="glass-card" style={{ padding: '24px', textDecoration: 'none', color: 'inherit' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '12px', borderRadius: '12px', background: 'rgba(99, 102, 241, 0.2)', color: 'var(--accent-primary)' }}>
              <FileCheck size={24} />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Resume Optimizer & ATS</h3>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Analyze resume bullet points with GPT-4o</p>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--accent-primary)', fontSize: '0.9rem', fontWeight: 600 }}>
            <span>Launch Agent</span>
            <ArrowUpRight size={16} />
          </div>
        </Link>

        <Link to="/career-roadmap" className="glass-card" style={{ padding: '24px', textDecoration: 'none', color: 'inherit' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '12px', borderRadius: '12px', background: 'rgba(236, 72, 153, 0.2)', color: 'var(--accent-pink)' }}>
              <Compass size={24} />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Career Progression Roadmap</h3>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Step-by-step technical transition milestones</p>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--accent-pink)', fontSize: '0.9rem', fontWeight: 600 }}>
            <span>View Roadmap</span>
            <ArrowUpRight size={16} />
          </div>
        </Link>

        <Link to="/interview" className="glass-card" style={{ padding: '24px', textDecoration: 'none', color: 'inherit' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '12px', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.2)', color: 'var(--accent-green)' }}>
              <Sparkles size={24} />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>AI Mock Interview Evaluator</h3>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Practice system design & Java questions</p>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--accent-green)', fontSize: '0.9rem', fontWeight: 600 }}>
            <span>Start Practice</span>
            <ArrowUpRight size={16} />
          </div>
        </Link>
      </div>

      {/* Recent Activity */}
      <div className="glass-card" style={{ padding: '24px', marginTop: '8px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Recent Platform Activity</h2>
          <Link to="/audit-logs" style={{ color: 'var(--accent-primary)', fontSize: '0.875rem', fontWeight: 600, textDecoration: 'none' }}>View Audit Logs →</Link>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {[
            { service: 'PROFILE_SERVICE', event: 'RESUME_UPLOADED', text: 'Uploaded resume resume_alex_architect.pdf', status: 'SUCCESS', time: '5 mins ago' },
            { service: 'AI_AGENT_SERVICE', event: 'AI_RECOMMENDATION_GENERATED', text: 'Generated 9-month career roadmap for Lead Architect', status: 'SUCCESS', time: '12 mins ago' },
            { service: 'AUTH_SERVICE', event: 'USER_LOGGED_IN', text: 'User authentication successful', status: 'SUCCESS', time: '25 mins ago' }
          ].map((item, idx) => (
            <div key={idx} style={{
              display: 'flex', justifyContent: 'space-between', alignItems: 'center',
              padding: '12px 16px', borderRadius: 'var(--radius-md)', background: 'rgba(255, 255, 255, 0.03)', border: '1px solid var(--border-color)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <CheckCircle size={18} color="var(--accent-green)" />
                <div>
                  <div style={{ fontSize: '0.9rem', fontWeight: 600 }}>{item.text}</div>
                  <span className="badge badge-purple" style={{ marginTop: '4px' }}>{item.service}</span>
                </div>
              </div>
              <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{item.time}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
