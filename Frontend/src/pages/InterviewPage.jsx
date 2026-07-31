import React, { useState } from 'react';
import { aiService } from '../services/aiService';
import {
  Sparkles, Play, Award, RefreshCw, Loader, CheckCircle,
  XCircle, ChevronRight, BookOpen, Clock, Target, BarChart3
} from 'lucide-react';

const QUESTION_BANK = [
  {
    id: 1, category: 'System Design', difficulty: 'Hard',
    question: 'Design a distributed rate limiter for CareerOS API Gateway that handles 100K RPS across multiple data centers.',
    keypoints: ['Token bucket / sliding window', 'Redis cluster with Lua scripts', 'Sticky routing for consistency', 'Fallback on Redis failure'],
    estimatedTime: '20 min'
  },
  {
    id: 2, category: 'Microservices', difficulty: 'Hard',
    question: 'How do you ensure exactly-once delivery semantics in a Kafka consumer that stores audit events in PostgreSQL?',
    keypoints: ['Idempotency key / Event ID dedup', 'Transactional outbox pattern', 'Dead Letter Queue (DLQ)', 'Kafka consumer group offsets'],
    estimatedTime: '15 min'
  },
  {
    id: 3, category: 'Spring Boot', difficulty: 'Medium',
    question: 'Explain how Spring Cloud Gateway performs JWT validation before routing to downstream microservices.',
    keypoints: ['GatewayFilter chain', 'ReactiveJwtDecoder', 'WebFilter vs GatewayFilter', 'X-User-Id header propagation'],
    estimatedTime: '10 min'
  },
  {
    id: 4, category: 'Database', difficulty: 'Medium',
    question: 'How would you implement database-per-service in a microservices architecture using PostgreSQL and Flyway?',
    keypoints: ['Schema isolation', 'Flyway migrations per service', 'Cross-service joins via API calls', 'Saga pattern for distributed transactions'],
    estimatedTime: '12 min'
  },
  {
    id: 5, category: 'Cloud / AWS', difficulty: 'Hard',
    question: 'Design the AWS infrastructure for CareerOS to achieve 99.99% uptime with auto-scaling and zero-downtime deployments.',
    keypoints: ['EKS + ALB + Route53', 'RDS Multi-AZ + ElastiCache', 'Blue-green / rolling deployments', 'CloudWatch alarms + Auto Scaling'],
    estimatedTime: '25 min'
  },
  {
    id: 6, category: 'Behavioral', difficulty: 'Easy',
    question: 'Describe a time you had to make a critical architectural decision under time pressure. What was the outcome?',
    keypoints: ['STAR format', 'Trade-off analysis', 'Stakeholder communication', 'Post-mortem learnings'],
    estimatedTime: '8 min'
  },
];

const DIFF_COLORS = {
  Easy: 'var(--accent-green)', Medium: 'var(--accent-yellow)', Hard: 'var(--accent-red)'
};

const MOCK_EVALUATION = {
  score_out_of_10: 8,
  grade: 'A-',
  strengths: ['Strong understanding of idempotency', 'Correctly mentioned DLQ pattern', 'Good awareness of distributed transactions'],
  improvements: ['Could elaborate on offset commit strategies', 'Mention Kafka transactions (exactly-once semantics API)'],
  ideal_sample_answer: 'For exactly-once delivery: 1) Store event_id in PostgreSQL with a UNIQUE constraint. 2) Use a DB transaction to both insert the event and commit the Kafka offset atomically (transactional outbox). 3) Implement DLQ for poison messages after 3 retry attempts. 4) Use Kafka\'s idempotent producer + transactions API for end-to-end EOS guarantees.',
  interview_tip: 'Always mention the trade-off between throughput and consistency when discussing exactly-once semantics.'
};

export const InterviewPage = () => {
  const [selectedQ, setSelectedQ] = useState(QUESTION_BANK[0]);
  const [userAnswer, setUserAnswer] = useState('');
  const [evaluation, setEvaluation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [timer, setTimer] = useState(0);
  const [timerActive, setTimerActive] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState('All');

  const categories = ['All', ...new Set(QUESTION_BANK.map(q => q.category))];

  const startTimer = () => {
    setTimer(0);
    setTimerActive(true);
    const iv = setInterval(() => setTimer(t => t + 1), 1000);
    setTimeout(() => clearInterval(iv), 30 * 60 * 1000);
  };

  const formatTime = (s) => `${String(Math.floor(s / 60)).padStart(2, '0')}:${String(s % 60).padStart(2, '0')}`;

  const handleEvaluate = async () => {
    if (!userAnswer.trim()) return;
    setLoading(true);
    setTimerActive(false);
    try {
      const res = await aiService.submitMockAnswer({
        question: selectedQ.question,
        user_answer: userAnswer,
        target_role: 'Senior Software Engineer',
        category: selectedQ.category
      });
      setEvaluation(res?.data || res || MOCK_EVALUATION);
    } catch {
      setEvaluation(MOCK_EVALUATION);
    } finally {
      setLoading(false);
    }
  };

  const handleNewQuestion = (q) => {
    setSelectedQ(q);
    setUserAnswer('');
    setEvaluation(null);
    setTimer(0);
    setTimerActive(false);
  };

  const filteredQuestions = categoryFilter === 'All'
    ? QUESTION_BANK
    : QUESTION_BANK.filter(q => q.category === categoryFilter);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>

      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Sparkles size={28} color="var(--accent-primary)" />
            AI Mock Interview Evaluator
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            Practice system design & behavioral questions — get instant GPT-4o scoring
          </p>
        </div>
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
          {timerActive && (
            <div style={{
              padding: '8px 16px', borderRadius: 'var(--radius-md)', background: 'rgba(99,102,241,0.12)',
              border: '1px solid rgba(99,102,241,0.3)', display: 'flex', alignItems: 'center', gap: '8px'
            }}>
              <Clock size={16} color="var(--accent-primary)" />
              <span style={{ fontFamily: 'monospace', fontWeight: 700, color: 'var(--accent-primary)' }}>{formatTime(timer)}</span>
            </div>
          )}
          <button onClick={startTimer} className="btn-secondary">
            <Play size={16} /><span>Start Timer</span>
          </button>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '300px 1fr', gap: '20px' }}>
        {/* Left: Question Bank */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {/* Category Filter */}
          <select className="form-select" value={categoryFilter}
            onChange={e => setCategoryFilter(e.target.value)}>
            {categories.map(c => <option key={c} value={c}>{c}</option>)}
          </select>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {filteredQuestions.map(q => (
              <button key={q.id} onClick={() => handleNewQuestion(q)}
                style={{
                  padding: '12px 14px', borderRadius: 'var(--radius-md)', cursor: 'pointer',
                  background: selectedQ.id === q.id ? 'var(--gradient-main)' : 'var(--bg-card)',
                  border: `1px solid ${selectedQ.id === q.id ? 'transparent' : 'var(--border-color)'}`,
                  color: selectedQ.id === q.id ? '#fff' : 'var(--text-primary)',
                  textAlign: 'left', transition: 'all 0.2s',
                  backdropFilter: 'blur(16px)'
                }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                  <span style={{
                    fontSize: '0.7rem', fontWeight: 700,
                    color: selectedQ.id === q.id ? 'rgba(255,255,255,0.8)' : DIFF_COLORS[q.difficulty]
                  }}>{q.category}</span>
                  <span style={{
                    fontSize: '0.7rem', fontWeight: 600,
                    color: selectedQ.id === q.id ? 'rgba(255,255,255,0.7)' : 'var(--text-muted)'
                  }}>{q.estimatedTime}</span>
                </div>
                <p style={{ fontSize: '0.8rem', lineHeight: 1.4, margin: 0,
                  color: selectedQ.id === q.id ? 'rgba(255,255,255,0.9)' : 'var(--text-secondary)' }}>
                  {q.question.slice(0, 80)}…
                </p>
                <span style={{
                  display: 'inline-block', marginTop: '6px', fontSize: '0.68rem', fontWeight: 700,
                  padding: '2px 8px', borderRadius: '9999px',
                  background: selectedQ.id === q.id ? 'rgba(255,255,255,0.2)' : `${DIFF_COLORS[q.difficulty]}20`,
                  color: selectedQ.id === q.id ? '#fff' : DIFF_COLORS[q.difficulty]
                }}>{q.difficulty}</span>
              </button>
            ))}
          </div>
        </div>

        {/* Right: Practice + Results */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {/* Question Card */}
          <div className="glass-card" style={{ padding: '24px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
              <div style={{ display: 'flex', gap: '8px' }}>
                <span style={{
                  fontSize: '0.75rem', fontWeight: 700, padding: '4px 10px',
                  borderRadius: '999px', background: 'rgba(99,102,241,0.12)', color: 'var(--accent-primary)'
                }}>{selectedQ.category}</span>
                <span style={{
                  fontSize: '0.75rem', fontWeight: 700, padding: '4px 10px', borderRadius: '999px',
                  background: `${DIFF_COLORS[selectedQ.difficulty]}15`, color: DIFF_COLORS[selectedQ.difficulty]
                }}>{selectedQ.difficulty}</span>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--text-muted)', fontSize: '0.8rem' }}>
                <Clock size={14} />{selectedQ.estimatedTime}
              </div>
            </div>

            <h2 style={{ fontSize: '1.1rem', fontWeight: 700, lineHeight: 1.5, marginBottom: '16px' }}>
              {selectedQ.question}
            </h2>

            {/* Key Points Hint */}
            <div style={{
              padding: '12px 16px', borderRadius: 'var(--radius-md)',
              background: 'rgba(99,102,241,0.06)', border: '1px solid rgba(99,102,241,0.15)'
            }}>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--accent-primary)', marginBottom: '8px' }}>
                💡 KEY CONCEPTS TO COVER
              </div>
              <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                {selectedQ.keypoints.map((k, i) => (
                  <span key={i} style={{
                    fontSize: '0.75rem', padding: '3px 10px', borderRadius: '9999px',
                    background: 'rgba(99,102,241,0.12)', color: 'var(--accent-primary)', border: '1px solid rgba(99,102,241,0.2)'
                  }}>{k}</span>
                ))}
              </div>
            </div>
          </div>

          {/* Answer Input */}
          {!evaluation && (
            <div className="glass-card" style={{ padding: '24px' }}>
              <label className="form-label" style={{ marginBottom: '10px', display: 'block' }}>
                Your Answer / Architecture Explanation
              </label>
              <textarea rows={8} className="form-input"
                placeholder="Structure your answer: Context → Design → Trade-offs → Production considerations..."
                value={userAnswer} onChange={e => setUserAnswer(e.target.value)}
                style={{ resize: 'vertical', fontFamily: 'inherit', fontSize: '0.9rem', marginBottom: '16px' }} />
              <button onClick={handleEvaluate} disabled={!userAnswer.trim() || loading}
                className="btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '14px' }}>
                {loading
                  ? <><Loader size={18} style={{ animation: 'spin 1s linear infinite' }} /><span>Evaluating with GPT-4o…</span></>
                  : <><Target size={18} /><span>Submit for AI Evaluation</span></>
                }
              </button>
            </div>
          )}

          {/* Evaluation Results */}
          {evaluation && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {/* Score */}
              <div className="glass-card" style={{ padding: '24px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                  <div style={{
                    width: '80px', height: '80px', borderRadius: '50%',
                    background: evaluation.score_out_of_10 >= 8 ? 'rgba(16,185,129,0.12)' : 'rgba(245,158,11,0.12)',
                    border: `3px solid ${evaluation.score_out_of_10 >= 8 ? 'var(--accent-green)' : 'var(--accent-yellow)'}`,
                    display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'
                  }}>
                    <span style={{
                      fontSize: '1.5rem', fontWeight: 900,
                      color: evaluation.score_out_of_10 >= 8 ? 'var(--accent-green)' : 'var(--accent-yellow)'
                    }}>{evaluation.score_out_of_10}</span>
                    <span style={{ fontSize: '0.65rem', color: 'var(--text-muted)' }}>/ 10</span>
                  </div>
                  <div>
                    <div style={{ fontSize: '1.3rem', fontWeight: 800 }}>
                      Grade: {evaluation.grade || (evaluation.score_out_of_10 >= 9 ? 'A+' : evaluation.score_out_of_10 >= 8 ? 'A-' : evaluation.score_out_of_10 >= 7 ? 'B+' : 'B')}
                    </div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Evaluated against Senior Engineer bar</div>
                  </div>
                  <button onClick={() => { setEvaluation(null); setUserAnswer(''); }} className="btn-secondary" style={{ marginLeft: 'auto' }}>
                    <RefreshCw size={16} /><span>Try Again</span>
                  </button>
                </div>
              </div>

              {/* Strengths */}
              <div className="glass-card" style={{ padding: '20px' }}>
                <h3 style={{ fontWeight: 700, marginBottom: '12px', color: 'var(--accent-green)', display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <CheckCircle size={18} />Strengths
                </h3>
                {evaluation.strengths.map((s, i) => (
                  <div key={i} style={{ display: 'flex', gap: '10px', marginBottom: '8px', fontSize: '0.875rem' }}>
                    <span style={{ color: 'var(--accent-green)', marginTop: '2px' }}>✓</span>
                    <span>{s}</span>
                  </div>
                ))}
              </div>

              {/* Improvements */}
              <div className="glass-card" style={{ padding: '20px' }}>
                <h3 style={{ fontWeight: 700, marginBottom: '12px', color: 'var(--accent-yellow)', display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <BarChart3 size={18} />Areas to Improve
                </h3>
                {evaluation.improvements.map((s, i) => (
                  <div key={i} style={{ display: 'flex', gap: '10px', marginBottom: '8px', fontSize: '0.875rem' }}>
                    <span style={{ color: 'var(--accent-yellow)' }}>→</span>
                    <span style={{ color: 'var(--text-secondary)' }}>{s}</span>
                  </div>
                ))}
              </div>

              {/* Model Answer */}
              <div className="glass-card" style={{ padding: '20px' }}>
                <h3 style={{ fontWeight: 700, marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <BookOpen size={18} color="var(--accent-primary)" />Model Answer
                </h3>
                <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: 1.7,
                  background: 'rgba(255,255,255,0.03)', padding: '14px', borderRadius: 'var(--radius-md)',
                  border: '1px solid var(--border-color)' }}>
                  {evaluation.ideal_sample_answer}
                </p>
                {evaluation.interview_tip && (
                  <div style={{ marginTop: '12px', padding: '10px 14px', borderRadius: 'var(--radius-sm)',
                    background: 'rgba(99,102,241,0.08)', border: '1px solid rgba(99,102,241,0.2)' }}>
                    <span style={{ fontSize: '0.8rem', color: 'var(--accent-primary)' }}>
                      💡 <strong>Interview Tip:</strong> {evaluation.interview_tip}
                    </span>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
