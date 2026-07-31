import React, { useState } from 'react';
import { aiService } from '../services/aiService';
import {
  Compass, Sparkles, Loader, ChevronRight, CheckCircle2,
  Clock, Zap, ArrowRight, BookOpen, Star
} from 'lucide-react';

const PHASE_COLORS = [
  'var(--accent-primary)', 'var(--accent-pink)', 'var(--accent-cyan)',
  'var(--accent-green)', 'var(--accent-yellow)'
];

const MOCK_ROADMAP = {
  target_role: 'Principal Software Architect',
  estimated_timeline_months: 12,
  current_skills_assessment: 'Strong backend engineering foundation. Ready for architectural leadership.',
  skill_gap_score: 34,
  milestones: [
    {
      phase: 'Phase 1: Architecture Fundamentals',
      timeframe: 'Months 1–2',
      goals: ['Master distributed systems patterns (CQRS, Event Sourcing, Saga)', 'Study the "Designing Data-Intensive Applications" book', 'Design 2 complex system design problems weekly'],
      recommended_skills: ['CQRS', 'Event Sourcing', 'CAP Theorem', 'Consensus Protocols'],
      resources: ['Martin Kleppmann — DDIA', 'Distributed Systems Course (MIT)', 'System Design Interview Vol. 2'],
    },
    {
      phase: 'Phase 2: Cloud Architecture & AWS',
      timeframe: 'Months 3–4',
      goals: ['Achieve AWS Solutions Architect Professional certification', 'Design multi-region active-active deployment', 'Implement IaC with Terraform for a production system'],
      recommended_skills: ['AWS EKS', 'Terraform', 'Multi-region DR', 'AWS Well-Architected'],
      resources: ['AWS SAP Exam Guide', 'Terraform: Up and Running', 'AWS Architecture Center'],
    },
    {
      phase: 'Phase 3: Leadership & Communication',
      timeframe: 'Months 5–7',
      goals: ['Lead 2 cross-team technical design reviews', 'Write 3 Architecture Decision Records (ADRs)', 'Mentor 2 junior engineers through a project'],
      recommended_skills: ['ADR Writing', 'RFC Process', 'Technical Roadmapping', 'Stakeholder Management'],
      resources: ['Staff Engineer by Will Larson', 'Architecture Decision Records Guide', 'The Manager\'s Path'],
    },
    {
      phase: 'Phase 4: Machine Learning Integration',
      timeframe: 'Months 8–10',
      goals: ['Understand MLOps pipelines and model serving', 'Integrate ML inference into existing microservices', 'Design a feature store for the CareerOS platform'],
      recommended_skills: ['MLflow', 'SageMaker', 'Vector Databases', 'Feature Engineering'],
      resources: ['Designing ML Systems by Chip Huyen', 'AWS SageMaker Workshop', 'Vector DB Comparison Guide'],
    },
    {
      phase: 'Phase 5: Job Search & Negotiation',
      timeframe: 'Months 11–12',
      goals: ['Target Principal/Staff Engineer roles at FAANG/growth companies', 'Complete 5 full system design loops', 'Negotiate total compensation > $250K'],
      recommended_skills: ['System Design Interviews', 'Behavioral STAR Stories', 'Compensation Negotiation'],
      resources: ['levels.fyi for comp benchmarks', 'Interview.io for mock practice', 'Cracking PM Interview for leadership Q&A'],
    },
  ]
};

const ROLE_PRESETS = [
  { from: 'Backend Developer', to: 'Senior Backend Engineer' },
  { from: 'Senior Backend Engineer', to: 'Principal Software Architect' },
  { from: 'Software Engineer', to: 'Engineering Manager' },
  { from: 'Frontend Developer', to: 'Full Stack Lead Engineer' },
  { from: 'Data Engineer', to: 'ML Engineer' },
];

export const CareerRoadmapPage = () => {
  const [currentRole, setCurrentRole] = useState('Senior Backend Engineer');
  const [targetRole, setTargetRole] = useState('Principal Software Architect');
  const [experience, setExperience] = useState('7');
  const [skills, setSkills] = useState('Java 21, Spring Boot, PostgreSQL, Kafka, Redis, Docker');
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(false);
  const [expandedPhase, setExpandedPhase] = useState(0);

  const handleGenerate = async () => {
    setLoading(true);
    setRoadmap(null);
    try {
      const res = await aiService.generateCareerRoadmap({
        current_role: currentRole,
        target_role: targetRole,
        years_of_experience: parseInt(experience),
        current_skills: skills.split(',').map(s => s.trim()).filter(Boolean),
      });
      setRoadmap(res?.data || res || MOCK_ROADMAP);
      setExpandedPhase(0);
    } catch {
      setRoadmap(MOCK_ROADMAP);
      setExpandedPhase(0);
    } finally {
      setLoading(false);
    }
  };

  const applyPreset = (p) => {
    setCurrentRole(p.from);
    setTargetRole(p.to);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <style>{`@keyframes spin { to { transform: rotate(360deg); } } @keyframes fadeIn { from { opacity: 0; transform: translateY(8px); } to { opacity: 1; transform: translateY(0); } }`}</style>

      {/* Header */}
      <div>
        <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Compass size={28} color="var(--accent-primary)" />
          AI Career Roadmap Generator
        </h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
          GPT-4o generates a personalized step-by-step career transition roadmap with resources & timelines
        </p>
      </div>

      {/* Config Card */}
      <div className="glass-card" style={{ padding: '28px' }}>
        {/* Quick Presets */}
        <div style={{ marginBottom: '20px' }}>
          <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', marginBottom: '10px' }}>
            Quick Presets
          </div>
          <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
            {ROLE_PRESETS.map((p, i) => (
              <button key={i} onClick={() => applyPreset(p)} className="btn-secondary"
                style={{ fontSize: '0.78rem', padding: '6px 12px' }}>
                {p.from} → {p.to}
              </button>
            ))}
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '16px', marginBottom: '20px' }}>
          <div className="form-group" style={{ margin: 0 }}>
            <label className="form-label">Current Role</label>
            <input type="text" className="form-input" value={currentRole}
              onChange={e => setCurrentRole(e.target.value)} placeholder="e.g. Senior Backend Engineer" />
          </div>
          <div className="form-group" style={{ margin: 0 }}>
            <label className="form-label">Target Role</label>
            <input type="text" className="form-input" value={targetRole}
              onChange={e => setTargetRole(e.target.value)} placeholder="e.g. Principal Architect" />
          </div>
          <div className="form-group" style={{ margin: 0 }}>
            <label className="form-label">Years of Experience</label>
            <input type="number" className="form-input" value={experience} min="0" max="30"
              onChange={e => setExperience(e.target.value)} />
          </div>
        </div>

        <div className="form-group" style={{ marginBottom: '20px' }}>
          <label className="form-label">Current Skills (comma-separated)</label>
          <input type="text" className="form-input" value={skills} onChange={e => setSkills(e.target.value)}
            placeholder="Java, Spring Boot, PostgreSQL, Kafka..." />
        </div>

        <button onClick={handleGenerate} disabled={loading || !currentRole || !targetRole}
          className="btn-primary" style={{ padding: '13px 32px' }}>
          {loading
            ? <><Loader size={18} style={{ animation: 'spin 1s linear infinite' }} /><span>Generating Roadmap…</span></>
            : <><Sparkles size={18} /><span>Generate AI Roadmap</span></>
          }
        </button>
      </div>

      {/* Roadmap Result */}
      {roadmap && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', animation: 'fadeIn 0.4s ease' }}>
          {/* Summary Banner */}
          <div style={{
            padding: '24px 28px', borderRadius: 'var(--radius-xl)',
            background: 'var(--gradient-main)', color: '#fff',
            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
            boxShadow: '0 8px 32px rgba(99,102,241,0.4)'
          }}>
            <div>
              <div style={{ fontSize: '1.3rem', fontWeight: 800 }}>
                {currentRole} <ArrowRight size={20} style={{ verticalAlign: 'middle', margin: '0 8px' }} /> {roadmap.target_role}
              </div>
              <div style={{ opacity: 0.85, fontSize: '0.875rem', marginTop: '6px' }}>
                {roadmap.current_skills_assessment}
              </div>
            </div>
            <div style={{ textAlign: 'center', background: 'rgba(255,255,255,0.15)', padding: '16px 24px', borderRadius: 'var(--radius-lg)' }}>
              <div style={{ fontSize: '2rem', fontWeight: 900 }}>{roadmap.estimated_timeline_months}</div>
              <div style={{ fontSize: '0.8rem', opacity: 0.9 }}>months</div>
            </div>
          </div>

          {/* Skill Gap Indicator */}
          {roadmap.skill_gap_score && (
            <div className="glass-card" style={{ padding: '20px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                <span style={{ fontWeight: 600, fontSize: '0.9rem' }}>Skill Gap to Target Role</span>
                <span style={{ color: 'var(--accent-yellow)', fontWeight: 700 }}>{roadmap.skill_gap_score}% gap remaining</span>
              </div>
              <div style={{ height: '8px', background: 'rgba(255,255,255,0.08)', borderRadius: '9999px', overflow: 'hidden' }}>
                <div style={{
                  height: '100%', background: 'var(--gradient-main)',
                  width: `${100 - roadmap.skill_gap_score}%`, borderRadius: '9999px',
                  transition: 'width 1s cubic-bezier(0.4,0,0.2,1)'
                }} />
              </div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '6px' }}>
                You are {100 - roadmap.skill_gap_score}% aligned with {roadmap.target_role}
              </div>
            </div>
          )}

          {/* Phase Timeline */}
          {roadmap.milestones.map((m, idx) => {
            const color = PHASE_COLORS[idx % PHASE_COLORS.length];
            const isExpanded = expandedPhase === idx;
            return (
              <div key={idx} style={{ position: 'relative' }}>
                {/* Connector line */}
                {idx < roadmap.milestones.length - 1 && (
                  <div style={{
                    position: 'absolute', left: '23px', top: '52px', bottom: '-20px',
                    width: '2px', background: `linear-gradient(to bottom, ${color}, transparent)`,
                    zIndex: 0
                  }} />
                )}
                <div className="glass-card" style={{
                  padding: '20px 24px', borderLeft: `4px solid ${color}`,
                  position: 'relative', zIndex: 1
                }}>
                  {/* Phase Header */}
                  <div onClick={() => setExpandedPhase(isExpanded ? -1 : idx)}
                    style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', cursor: 'pointer' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
                      <div style={{
                        width: '42px', height: '42px', borderRadius: '50%', flexShrink: 0,
                        background: `${color}20`, border: `2px solid ${color}`,
                        display: 'flex', alignItems: 'center', justifyContent: 'center',
                        fontWeight: 800, color, fontSize: '1rem'
                      }}>{idx + 1}</div>
                      <div>
                        <h3 style={{ fontWeight: 700, fontSize: '1rem' }}>{m.phase}</h3>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginTop: '2px' }}>
                          <Clock size={13} color="var(--text-muted)" />
                          <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{m.timeframe}</span>
                        </div>
                      </div>
                    </div>
                    <ChevronRight size={20} color="var(--text-muted)"
                      style={{ transform: isExpanded ? 'rotate(90deg)' : 'none', transition: 'transform 0.2s' }} />
                  </div>

                  {/* Expanded Content */}
                  {isExpanded && (
                    <div style={{ marginTop: '20px', display: 'flex', flexDirection: 'column', gap: '16px', animation: 'fadeIn 0.3s ease' }}>
                      {/* Goals */}
                      <div>
                        <div style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', marginBottom: '10px' }}>
                          🎯 Goals
                        </div>
                        {m.goals.map((g, gi) => (
                          <div key={gi} style={{ display: 'flex', gap: '10px', marginBottom: '8px', fontSize: '0.875rem' }}>
                            <CheckCircle2 size={16} color={color} style={{ flexShrink: 0, marginTop: '2px' }} />
                            <span>{g}</span>
                          </div>
                        ))}
                      </div>

                      {/* Skills */}
                      <div>
                        <div style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', marginBottom: '10px' }}>
                          <Zap size={13} style={{ display: 'inline', marginRight: '4px' }} />Skills to Acquire
                        </div>
                        <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                          {m.recommended_skills.map((s, si) => (
                            <span key={si} style={{
                              padding: '4px 12px', borderRadius: '9999px', fontSize: '0.78rem', fontWeight: 600,
                              background: `${color}15`, color, border: `1px solid ${color}30`
                            }}>{s}</span>
                          ))}
                        </div>
                      </div>

                      {/* Resources */}
                      {m.resources && (
                        <div>
                          <div style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', marginBottom: '10px' }}>
                            <BookOpen size={13} style={{ display: 'inline', marginRight: '4px' }} />Recommended Resources
                          </div>
                          {m.resources.map((r, ri) => (
                            <div key={ri} style={{ display: 'flex', gap: '8px', marginBottom: '6px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                              <Star size={14} color={color} style={{ flexShrink: 0, marginTop: '2px' }} />
                              <span>{r}</span>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};
