import React, { useState, useRef } from 'react';
import { aiService } from '../services/aiService';
import {
  FileText, Upload, Sparkles, Target, CheckCircle, AlertCircle,
  Loader, ClipboardCopy, CheckCheck, TrendingUp, Zap
} from 'lucide-react';

const ScoreRing = ({ score, size = 120 }) => {
  const radius = (size - 20) / 2;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference - (score / 100) * circumference;
  const color = score >= 80 ? 'var(--accent-green)' : score >= 60 ? 'var(--accent-yellow)' : 'var(--accent-red)';

  return (
    <svg width={size} height={size} style={{ transform: 'rotate(-90deg)' }}>
      <circle cx={size / 2} cy={size / 2} r={radius}
        fill="none" stroke="rgba(255,255,255,0.06)" strokeWidth={10} />
      <circle cx={size / 2} cy={size / 2} r={radius}
        fill="none" stroke={color} strokeWidth={10}
        strokeDasharray={circumference} strokeDashoffset={offset}
        strokeLinecap="round"
        style={{ transition: 'stroke-dashoffset 1.2s cubic-bezier(0.4, 0, 0.2, 1)' }} />
      <text x="50%" y="50%" textAnchor="middle" dominantBaseline="central"
        style={{ fill: color, fontSize: `${size * 0.2}px`, fontWeight: 800, transform: 'rotate(90deg)', transformOrigin: 'center' }}>
        {score}
      </text>
    </svg>
  );
};

const MOCK_ANALYSIS = {
  atsScore: 72,
  overallScore: 78,
  improvements: [
    { type: 'CRITICAL', icon: '🚨', text: 'Missing quantified metrics — add numbers to all achievements (e.g., "Reduced latency by 40%")' },
    { type: 'HIGH', icon: '⚠️', text: 'No keywords matching target JD: "distributed systems", "Kafka", "microservices" — add these' },
    { type: 'HIGH', icon: '⚠️', text: 'Summary section too generic — tailor it to target role specifically' },
    { type: 'MEDIUM', icon: '💡', text: 'Move certifications before education section for stronger impact' },
    { type: 'LOW', icon: '✨', text: 'Consider adding a GitHub/Portfolio URL in the contact section' },
  ],
  strengths: ['Strong technical stack listed', 'Clear career progression visible', 'Good use of action verbs in experience'],
  improvedBullets: [
    { original: 'Worked on backend APIs', improved: 'Architected and deployed 12 production REST APIs serving 2M+ daily requests with 99.9% uptime' },
    { original: 'Helped with database optimization', improved: 'Reduced PostgreSQL query latency by 65% through strategic indexing and query optimization, saving $8K/month in compute' },
  ]
};

export const ResumePage = () => {
  const [resumeText, setResumeText] = useState('');
  const [jobDescription, setJobDescription] = useState('');
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('analyze');
  const [copied, setCopied] = useState(null);
  const fileRef = useRef(null);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (ev) => setResumeText(ev.target.result);
    reader.readAsText(file);
  };

  const handleAnalyze = async () => {
    if (!resumeText.trim()) return;
    setLoading(true);
    try {
      let result;
      if (jobDescription.trim()) {
        result = await aiService.getAtsScore(resumeText, jobDescription);
      } else {
        result = await aiService.analyzeResume(null);
      }
      setAnalysis(result?.data || result || MOCK_ANALYSIS);
    } catch {
      setAnalysis(MOCK_ANALYSIS); // offline fallback
    } finally {
      setLoading(false);
    }
  };

  const handleImprove = async () => {
    if (!resumeText.trim()) return;
    setLoading(true);
    try {
      const result = await aiService.improveResume(resumeText);
      setAnalysis(prev => ({ ...prev, ...(result?.data || result || {}) }));
    } catch {
      setAnalysis(MOCK_ANALYSIS);
    } finally {
      setLoading(false);
    }
  };

  const copyText = (text, id) => {
    navigator.clipboard.writeText(text);
    setCopied(id);
    setTimeout(() => setCopied(null), 2000);
  };

  const TABS = [
    { id: 'analyze', label: '🔍 ATS Analyzer' },
    { id: 'improve', label: '✨ AI Rewriter' },
    { id: 'results', label: '📊 Results', disabled: !analysis },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>

      {/* Header */}
      <div>
        <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
          <FileText size={28} color="var(--accent-primary)" />
          Resume Optimizer & ATS Analyzer
        </h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
          AI-powered resume analysis with GPT-4o · ATS keyword matching · Bullet point rewriting
        </p>
      </div>

      {/* Tab Bar */}
      <div style={{ display: 'flex', gap: '4px', background: 'rgba(255,255,255,0.04)', padding: '4px', borderRadius: 'var(--radius-md)', width: 'fit-content' }}>
        {TABS.map(tab => (
          <button key={tab.id} onClick={() => !tab.disabled && setActiveTab(tab.id)}
            disabled={tab.disabled}
            style={{
              padding: '10px 20px', borderRadius: 'calc(var(--radius-md) - 2px)',
              border: 'none', cursor: tab.disabled ? 'not-allowed' : 'pointer',
              background: activeTab === tab.id ? 'var(--gradient-main)' : 'transparent',
              color: tab.disabled ? 'var(--text-muted)' : activeTab === tab.id ? '#fff' : 'var(--text-secondary)',
              fontWeight: activeTab === tab.id ? 700 : 500, fontSize: '0.875rem',
              transition: 'all 0.2s', boxShadow: activeTab === tab.id ? '0 4px 14px rgba(99,102,241,0.4)' : 'none'
            }}>
            {tab.label}
          </button>
        ))}
      </div>

      {/* ─── ATS Analyzer Tab ─────────────────────────────────────────────────── */}
      {activeTab === 'analyze' && (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
          {/* Resume Input */}
          <div className="glass-card" style={{ padding: '24px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
              <h3 style={{ fontWeight: 700 }}>Your Resume</h3>
              <button onClick={() => fileRef.current?.click()} className="btn-secondary" style={{ fontSize: '0.8rem', padding: '8px 14px' }}>
                <Upload size={14} /><span>Upload File</span>
              </button>
              <input ref={fileRef} type="file" accept=".txt,.pdf,.doc,.docx" hidden onChange={handleFileUpload} />
            </div>
            <textarea className="form-input" rows={14}
              placeholder="Paste your resume text here, or upload a .txt file above…"
              value={resumeText} onChange={e => setResumeText(e.target.value)}
              style={{ resize: 'vertical', fontFamily: 'monospace', fontSize: '0.85rem' }} />
          </div>

          {/* Job Description Input */}
          <div className="glass-card" style={{ padding: '24px' }}>
            <h3 style={{ fontWeight: 700, marginBottom: '16px' }}>Target Job Description</h3>
            <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '12px' }}>
              Optional — paste the job description to get ATS keyword match score
            </p>
            <textarea className="form-input" rows={14}
              placeholder="Paste the target job description here for ATS matching…"
              value={jobDescription} onChange={e => setJobDescription(e.target.value)}
              style={{ resize: 'vertical', fontSize: '0.85rem' }} />
          </div>

          {/* Analyze Button */}
          <div style={{ gridColumn: '1 / -1', display: 'flex', justifyContent: 'center', gap: '12px' }}>
            <button onClick={handleAnalyze} disabled={!resumeText.trim() || loading}
              className="btn-primary" style={{ padding: '14px 36px', fontSize: '1rem' }}>
              {loading
                ? <><Loader size={20} style={{ animation: 'spin 1s linear infinite' }} /><span>Analyzing with AI…</span></>
                : <><Target size={20} /><span>Analyze Resume</span></>
              }
            </button>
            {analysis && (
              <button onClick={() => setActiveTab('results')} className="btn-secondary" style={{ padding: '14px 24px' }}>
                <TrendingUp size={18} /><span>View Results</span>
              </button>
            )}
          </div>
        </div>
      )}

      {/* ─── AI Rewriter Tab ──────────────────────────────────────────────────── */}
      {activeTab === 'improve' && (
        <div className="glass-card" style={{ padding: '28px' }}>
          <h3 style={{ fontWeight: 700, marginBottom: '8px' }}>AI Bullet Point Rewriter</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', marginBottom: '20px' }}>
            Paste your weak bullet points below. AI will rewrite them with specific impact metrics and strong action verbs.
          </p>
          <textarea className="form-input" rows={8}
            placeholder="Paste your resume bullet points here…"
            value={resumeText} onChange={e => setResumeText(e.target.value)}
            style={{ fontFamily: 'monospace', fontSize: '0.875rem', marginBottom: '16px' }} />
          <button onClick={handleImprove} disabled={!resumeText.trim() || loading}
            className="btn-primary" style={{ padding: '12px 28px' }}>
            {loading
              ? <><Loader size={18} style={{ animation: 'spin 1s linear infinite' }} /><span>Rewriting…</span></>
              : <><Zap size={18} /><span>Rewrite with AI</span></>
            }
          </button>
        </div>
      )}

      {/* ─── Results Tab ─────────────────────────────────────────────────────── */}
      {activeTab === 'results' && analysis && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Score cards */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
            {[
              { label: 'ATS Match Score', score: analysis.atsScore, desc: 'Keyword alignment with job description' },
              { label: 'Overall Quality', score: analysis.overallScore, desc: 'Structure, impact, and clarity' },
            ].map(s => (
              <div key={s.label} className="glass-card" style={{ padding: '24px', textAlign: 'center' }}>
                <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '12px' }}>
                  <ScoreRing score={s.score} />
                </div>
                <h3 style={{ fontWeight: 700, marginBottom: '4px' }}>{s.label}</h3>
                <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{s.desc}</p>
              </div>
            ))}

            {/* Strengths */}
            <div className="glass-card" style={{ padding: '24px' }}>
              <h3 style={{ fontWeight: 700, marginBottom: '12px', color: 'var(--accent-green)' }}>✅ Strengths</h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                {(analysis.strengths || []).map((s, i) => (
                  <div key={i} style={{ display: 'flex', gap: '8px', fontSize: '0.875rem' }}>
                    <CheckCircle size={16} color="var(--accent-green)" style={{ flexShrink: 0, marginTop: '2px' }} />
                    <span>{s}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Improvement Suggestions */}
          <div className="glass-card" style={{ padding: '24px' }}>
            <h3 style={{ fontWeight: 700, marginBottom: '16px' }}>🎯 Improvement Suggestions</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {(analysis.improvements || []).map((imp, i) => (
                <div key={i} style={{
                  padding: '14px 16px', borderRadius: 'var(--radius-md)', display: 'flex', gap: '12px',
                  background: imp.type === 'CRITICAL' ? 'rgba(239,68,68,0.08)' : imp.type === 'HIGH' ? 'rgba(245,158,11,0.08)' : 'rgba(255,255,255,0.04)',
                  border: `1px solid ${imp.type === 'CRITICAL' ? 'rgba(239,68,68,0.25)' : imp.type === 'HIGH' ? 'rgba(245,158,11,0.25)' : 'var(--border-color)'}`
                }}>
                  <span style={{ fontSize: '1.1rem', flexShrink: 0 }}>{imp.icon}</span>
                  <div>
                    <span style={{
                      fontSize: '0.7rem', fontWeight: 700, marginBottom: '4px', display: 'block',
                      color: imp.type === 'CRITICAL' ? '#f87171' : imp.type === 'HIGH' ? '#fbbf24' : 'var(--text-muted)'
                    }}>{imp.type}</span>
                    <span style={{ fontSize: '0.875rem' }}>{imp.text}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Before/After Bullets */}
          {analysis.improvedBullets?.length > 0 && (
            <div className="glass-card" style={{ padding: '24px' }}>
              <h3 style={{ fontWeight: 700, marginBottom: '16px' }}>✨ AI-Rewritten Bullet Points</h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {analysis.improvedBullets.map((bullet, i) => (
                  <div key={i}>
                    <div style={{ padding: '12px', borderRadius: 'var(--radius-sm)', background: 'rgba(239,68,68,0.08)', border: '1px solid rgba(239,68,68,0.2)', marginBottom: '8px' }}>
                      <div style={{ fontSize: '0.72rem', color: '#f87171', fontWeight: 700, marginBottom: '4px' }}>❌ BEFORE</div>
                      <span style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>{bullet.original}</span>
                    </div>
                    <div style={{ padding: '12px', borderRadius: 'var(--radius-sm)', background: 'rgba(16,185,129,0.08)', border: '1px solid rgba(16,185,129,0.2)' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                        <span style={{ fontSize: '0.72rem', color: 'var(--accent-green)', fontWeight: 700 }}>✅ AFTER</span>
                        <button onClick={() => copyText(bullet.improved, `bullet-${i}`)}
                          style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}>
                          {copied === `bullet-${i}` ? <CheckCheck size={14} color="var(--accent-green)" /> : <ClipboardCopy size={14} />}
                        </button>
                      </div>
                      <span style={{ fontSize: '0.875rem', fontWeight: 500 }}>{bullet.improved}</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
