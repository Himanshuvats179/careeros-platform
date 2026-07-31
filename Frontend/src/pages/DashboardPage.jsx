import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Sparkles, FileCheck, Compass, Activity, ArrowUpRight, CheckCircle, Search, MapPin, Briefcase, DollarSign, Bookmark, Send, Filter, Building, Bot } from 'lucide-react';
import { Link } from 'react-router-dom';

export const DashboardPage = () => {
  const { user } = useAuth();
  const [activeFilter, setActiveFilter] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [bookmarkedJobs, setBookmarkedJobs] = useState(['job-101']);
  const [appliedJobs, setAppliedJobs] = useState([]);
  const [aiApplyModal, setAiApplyModal] = useState(null);

  // Demo LinkedIn-style jobs list
  const sampleJobs = [
    {
      id: 'job-101',
      title: 'Senior Java Spring Boot Engineer',
      company: 'TechCorp Solutions',
      logo: '🚀',
      location: 'Bangalore, India',
      salary: '$130,000 - $165,000',
      type: 'FULL_TIME',
      remote: true,
      matchScore: 94,
      skills: ['Java 21', 'Spring Boot 3', 'Kafka', 'PostgreSQL', 'Redis'],
      posted: '2 hours ago',
      description: 'Join our high-throughput backend architecture team building distributed microservices using Java 21, Spring Boot 3, Kafka, and PostgreSQL.'
    },
    {
      id: 'job-102',
      title: 'Lead Staff Software Architect',
      company: 'CloudScale Systems',
      logo: '⚡',
      location: 'San Francisco, CA',
      salary: '$170,000 - $220,000',
      type: 'FULL_TIME',
      remote: true,
      matchScore: 89,
      skills: ['Java 21', 'Spring Cloud', 'Kubernetes', 'AWS', 'Docker'],
      posted: '5 hours ago',
      description: 'Lead platform architecture transition towards reactive microservices, Kubernetes container orchestration, and AI-assisted workflows.'
    },
    {
      id: 'job-103',
      title: 'AI Systems & Backend Platform Engineer',
      company: 'AI Next Labs',
      logo: '🤖',
      location: 'Remote',
      salary: '$140,000 - $185,000',
      type: 'FULL_TIME',
      remote: true,
      matchScore: 96,
      skills: ['Python', 'FastAPI', 'ChromaDB', 'LangChain', 'PyTorch'],
      posted: '1 day ago',
      description: 'Build high-performance FastAPI microservices integrating RAG vector search (ChromaDB), LangChain multi-agent workflows, and LLM inference endpoints.'
    }
  ];

  const toggleBookmark = (id) => {
    if (bookmarkedJobs.includes(id)) {
      setBookmarkedJobs(bookmarkedJobs.filter(jId => jId !== id));
    } else {
      setBookmarkedJobs([...bookmarkedJobs, id]);
    }
  };

  const handleManualApply = (job) => {
    if (!appliedJobs.includes(job.id)) {
      setAppliedJobs([...appliedJobs, job.id]);
    }
  };

  const filteredJobs = sampleJobs.filter(job => {
    if (activeFilter === 'REMOTE' && !job.remote) return false;
    if (activeFilter === 'SAVED' && !bookmarkedJobs.includes(job.id)) return false;
    if (activeFilter === 'AI_MATCH' && job.matchScore < 90) return false;
    if (searchQuery.trim() !== '') {
      const q = searchQuery.toLowerCase();
      return job.title.toLowerCase().includes(q) || job.company.toLowerCase().includes(q) || job.skills.some(s => s.toLowerCase().includes(q));
    }
    return true;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Executive Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '2rem', fontWeight: 800 }}>Welcome back, {user?.name || 'Alex'} 👋</h1>
          <p style={{ color: 'var(--text-secondary)' }}>CareerOS Job Feed & AI Career Intelligence Platform</p>
        </div>
        <Link to="/ai-chat" className="btn-primary">
          <Sparkles size={18} />
          <span>Ask AI Coach</span>
        </Link>
      </div>

      {/* Metric Highlights */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px' }}>
        <div className="glass-card" style={{ padding: '18px' }}>
          <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Profile Match</span>
          <div style={{ fontSize: '1.75rem', fontWeight: 800, marginTop: '4px', color: 'var(--accent-green)' }}>94% Match</div>
          <span className="badge badge-green" style={{ marginTop: '8px' }}>Senior Architect Profile</span>
        </div>

        <div className="glass-card" style={{ padding: '18px' }}>
          <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Saved / Bookmarked</span>
          <div style={{ fontSize: '1.75rem', fontWeight: 800, marginTop: '4px', color: 'var(--accent-primary)' }}>{bookmarkedJobs.length} Jobs</div>
          <span className="badge badge-purple" style={{ marginTop: '8px' }}>Saved for later</span>
        </div>

        <div className="glass-card" style={{ padding: '18px' }}>
          <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Active Applications</span>
          <div style={{ fontSize: '1.75rem', fontWeight: 800, marginTop: '4px', color: 'var(--accent-pink)' }}>{appliedJobs.length + 2} Applied</div>
          <span className="badge badge-pink" style={{ marginTop: '8px' }}>Tracking Live</span>
        </div>

        <div className="glass-card" style={{ padding: '18px' }}>
          <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>AI ATS Rating</span>
          <div style={{ fontSize: '1.75rem', fontWeight: 800, marginTop: '4px', color: 'var(--accent-cyan)' }}>88/100</div>
          <span className="badge badge-cyan" style={{ marginTop: '8px' }}>Optimal Resume</span>
        </div>
      </div>

      {/* Main Grid: LinkedIn-Style Jobs Feed & AI Features Sidebar */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: '24px' }}>
        
        {/* Left Column: Job Feed */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* Search & Filter Bar */}
          <div className="glass-card" style={{ padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div style={{ display: 'flex', alignItems: 'center', background: 'rgba(255, 255, 255, 0.05)', borderRadius: 'var(--radius-md)', padding: '10px 16px', gap: '12px', border: '1px solid var(--border-color)' }}>
              <Search size={18} color="var(--text-muted)" />
              <input
                type="text"
                placeholder="Search jobs by title, company, or skills (e.g. Java, Spring Boot, Remote)..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ background: 'transparent', border: 'none', color: '#fff', width: '100%', outline: 'none', fontSize: '0.95rem' }}
              />
            </div>

            {/* Quick Filter Badges */}
            <div style={{ display: 'flex', gap: '10px', overflowX: 'auto', paddingBottom: '4px' }}>
              {[
                { id: 'ALL', label: 'All Jobs' },
                { id: 'AI_MATCH', label: '✨ Top AI Match (90%+)' },
                { id: 'REMOTE', label: '🌐 Remote Only' },
                { id: 'SAVED', label: '🔖 Saved Jobs' }
              ].map(f => (
                <button
                  key={f.id}
                  onClick={() => setActiveFilter(f.id)}
                  className={activeFilter === f.id ? 'btn-primary' : 'btn-secondary'}
                  style={{ fontSize: '0.825rem', padding: '6px 14px', borderRadius: '20px' }}
                >
                  {f.label}
                </button>
              ))}
            </div>
          </div>

          {/* Job Cards */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {filteredJobs.length === 0 ? (
              <div className="glass-card" style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}>
                No jobs match your search criteria. Try clearing filters.
              </div>
            ) : (
              filteredJobs.map(job => {
                const isBookmarked = bookmarkedJobs.includes(job.id);
                const isApplied = appliedJobs.includes(job.id);

                return (
                  <div key={job.id} className="glass-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    
                    {/* Header Row */}
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                      <div style={{ display: 'flex', gap: '14px', alignItems: 'center' }}>
                        <div style={{ fontSize: '2rem', padding: '12px', borderRadius: '12px', background: 'rgba(255, 255, 255, 0.05)', border: '1px solid var(--border-color)' }}>
                          {job.logo}
                        </div>
                        <div>
                          <h3 style={{ fontSize: '1.25rem', fontWeight: 700, marginBottom: '4px' }}>{job.title}</h3>
                          <div style={{ display: 'flex', gap: '12px', alignItems: 'center', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                            <span style={{ fontWeight: 600, color: '#fff' }}>{job.company}</span>
                            <span>•</span>
                            <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}><MapPin size={14} />{job.location}</span>
                          </div>
                        </div>
                      </div>

                      {/* AI Match Badge & Bookmark Button */}
                      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        <span className="badge badge-green" style={{ fontSize: '0.85rem', padding: '6px 12px' }}>
                          ✨ {job.matchScore}% Match
                        </span>
                        <button
                          onClick={() => toggleBookmark(job.id)}
                          style={{ background: 'none', border: 'none', cursor: 'pointer', color: isBookmarked ? 'var(--accent-primary)' : 'var(--text-muted)' }}
                        >
                          <Bookmark size={20} fill={isBookmarked ? 'var(--accent-primary)' : 'none'} />
                        </button>
                      </div>
                    </div>

                    {/* Job Details & Description */}
                    <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', lineHeight: '1.5' }}>
                      {job.description}
                    </p>

                    {/* Tags Row */}
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                      <span className="badge badge-purple">{job.salary}</span>
                      <span className="badge badge-cyan">{job.type}</span>
                      {job.remote && <span className="badge badge-green">Remote</span>}
                      {job.skills.map((skill, sIdx) => (
                        <span key={sIdx} style={{ fontSize: '0.75rem', padding: '4px 10px', borderRadius: '12px', background: 'rgba(255, 255, 255, 0.04)', color: 'var(--text-secondary)', border: '1px solid var(--border-color)' }}>
                          {skill}
                        </span>
                      ))}
                    </div>

                    {/* Action Buttons Row */}
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '12px', borderTop: '1px solid var(--border-color)' }}>
                      <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Posted {job.posted}</span>
                      
                      <div style={{ display: 'flex', gap: '12px' }}>
                        <button
                          onClick={() => setAiApplyModal(job)}
                          className="btn-secondary"
                          style={{ border: '1px solid var(--accent-pink)', color: 'var(--accent-pink)', fontSize: '0.875rem' }}
                        >
                          <Sparkles size={16} />
                          <span>Apply with AI 🪄</span>
                        </button>

                        <button
                          onClick={() => handleManualApply(job)}
                          className={isApplied ? 'btn-secondary' : 'btn-primary'}
                          disabled={isApplied}
                          style={{ fontSize: '0.875rem' }}
                        >
                          <Send size={16} />
                          <span>{isApplied ? 'Applied ✓' : 'Apply Now'}</span>
                        </button>
                      </div>
                    </div>
                  </div>
                );
              })
            )}
          </div>
        </div>

        {/* Right Column: AI Features & Quick Tools */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* AI Platform Assistant Card */}
          <div className="glass-card" style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '14px', background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(236, 72, 153, 0.15) 100%)', border: '1px solid rgba(99, 102, 241, 0.3)' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <Bot size={22} color="var(--accent-primary)" />
              <h3 style={{ fontSize: '1.05rem', fontWeight: 700 }}>CareerOS AI Suite</h3>
            </div>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', lineHeight: '1.4' }}>
              Powered by FastAPI Multi-Agent RAG Engine, Chroma Vector DB & Ollama / OpenAI LLM Providers.
            </p>
            <Link to="/ai-chat" className="btn-primary" style={{ justifyContent: 'center', fontSize: '0.85rem' }}>
              <Sparkles size={16} />
              <span>Launch AI Chat Assistant</span>
            </Link>
          </div>

          {/* Quick AI Agent Shortcuts */}
          <div className="glass-card" style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <h3 style={{ fontSize: '1rem', fontWeight: 700 }}>AI Features Menu</h3>
            
            <Link to="/resume" style={{ display: 'flex', alignItems: 'center', gap: '12px', textDecoration: 'none', color: 'inherit', padding: '10px', borderRadius: '8px', background: 'rgba(255, 255, 255, 0.03)' }}>
              <FileCheck size={20} color="var(--accent-primary)" />
              <div>
                <div style={{ fontSize: '0.9rem', fontWeight: 600 }}>Resume & ATS Match</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Analyze & rewrite bullet points</div>
              </div>
            </Link>

            <Link to="/career-roadmap" style={{ display: 'flex', alignItems: 'center', gap: '12px', textDecoration: 'none', color: 'inherit', padding: '10px', borderRadius: '8px', background: 'rgba(255, 255, 255, 0.03)' }}>
              <Compass size={20} color="var(--accent-pink)" />
              <div>
                <div style={{ fontSize: '0.9rem', fontWeight: 600 }}>Career Progression Roadmap</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>9-month transition plan</div>
              </div>
            </Link>

            <Link to="/interview" style={{ display: 'flex', alignItems: 'center', gap: '12px', textDecoration: 'none', color: 'inherit', padding: '10px', borderRadius: '8px', background: 'rgba(255, 255, 255, 0.03)' }}>
              <Sparkles size={20} color="var(--accent-green)" />
              <div>
                <div style={{ fontSize: '0.9rem', fontWeight: 600 }}>Mock Interview Coach</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>STAR answer grading</div>
              </div>
            </Link>

            <Link to="/job-tracker" style={{ display: 'flex', alignItems: 'center', gap: '12px', textDecoration: 'none', color: 'inherit', padding: '10px', borderRadius: '8px', background: 'rgba(255, 255, 255, 0.03)' }}>
              <Briefcase size={20} color="var(--accent-cyan)" />
              <div>
                <div style={{ fontSize: '0.9rem', fontWeight: 600 }}>Application Tracker</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Track interview stages</div>
              </div>
            </Link>
          </div>
        </div>
      </div>

      {/* AI Apply Package Modal */}
      {aiApplyModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.7)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000, padding: '20px' }}>
          <div className="glass-card" style={{ maxWidth: '650px', width: '100%', padding: '32px', background: '#111827', border: '1px solid var(--border-color)', borderRadius: '16px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <Sparkles size={24} color="var(--accent-pink)" />
                <h2 style={{ fontSize: '1.3rem', fontWeight: 800 }}>Apply with AI: {aiApplyModal.title}</h2>
              </div>
              <button onClick={() => setAiApplyModal(null)} style={{ background: 'none', border: 'none', color: '#fff', cursor: 'pointer', fontSize: '1.2rem' }}>✕</button>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px', marginBottom: '24px' }}>
              <div style={{ padding: '14px', borderRadius: '10px', background: 'rgba(16, 185, 129, 0.1)', border: '1px solid rgba(16, 185, 129, 0.3)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span>ATS Compatibility Match Score:</span>
                <span style={{ fontSize: '1.2rem', fontWeight: 800, color: 'var(--accent-green)' }}>{aiApplyModal.matchScore}% Match</span>
              </div>

              <div>
                <label style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>AI Generated Cover Letter (Candidate Review & Approval):</label>
                <textarea
                  rows={6}
                  readOnly
                  value={`Dear Hiring Team at ${aiApplyModal.company},\n\nI am writing to express my enthusiastic interest in the ${aiApplyModal.title} position. With my background in Java 21, Spring Boot microservices, Kafka event streaming, and AI integrations, I am confident in making an immediate impact on your engineering team.\n\nThank you for your consideration.\n\nSincerely,\n${user?.name || 'Alex'}`}
                  style={{ width: '100%', padding: '12px', borderRadius: '8px', background: 'rgba(255, 255, 255, 0.05)', border: '1px solid var(--border-color)', color: '#fff', fontSize: '0.875rem' }}
                />
              </div>
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px' }}>
              <button onClick={() => setAiApplyModal(null)} className="btn-secondary">Cancel</button>
              <button
                onClick={() => {
                  handleManualApply(aiApplyModal);
                  setAiApplyModal(null);
                }}
                className="btn-primary"
              >
                Approve & Submit Application
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
