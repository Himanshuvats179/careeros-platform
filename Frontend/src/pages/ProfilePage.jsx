import React, { useState, useEffect, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { profileService } from '../services/profileService';
import {
  User, Briefcase, GraduationCap, Code, Award, Upload,
  Plus, Trash2, Save, Loader, CheckCircle, Globe, Phone,
  MapPin, Github, Linkedin, Edit3
} from 'lucide-react';

const LEVEL_COLORS = { EXPERT: 'var(--accent-green)', ADVANCED: 'var(--accent-cyan)', INTERMEDIATE: 'var(--accent-primary)', BEGINNER: 'var(--text-muted)' };

const TABS = [
  { id: 'personal',    label: 'Personal Info',      icon: User },
  { id: 'skills',      label: 'Skills & Stack',     icon: Code },
  { id: 'experience',  label: 'Experience',         icon: Briefcase },
  { id: 'education',   label: 'Education',          icon: GraduationCap },
  { id: 'resume',      label: 'Resumes',            icon: Award },
];

const MOCK_PROFILE = {
  firstName: 'Alex', lastName: 'Rivera',
  headline: 'Senior Software Architect | Java 21 & AI Systems',
  bio: 'Passionate architect with 10+ years engineering high-throughput microservices, event-driven systems (Kafka), and AI agent platforms on AWS.',
  phone: '+1 (555) 234-5678', location: 'San Francisco, CA',
  github: 'https://github.com/alexrivera', linkedin: 'https://linkedin.com/in/alexrivera'
};

const MOCK_SKILLS = [
  { id: '1', name: 'Java 21', level: 'EXPERT', category: 'Backend' },
  { id: '2', name: 'Spring Boot 3', level: 'EXPERT', category: 'Backend' },
  { id: '3', name: 'Apache Kafka', level: 'ADVANCED', category: 'Distributed Systems' },
  { id: '4', name: 'FastAPI + LangChain', level: 'ADVANCED', category: 'AI & Python' },
  { id: '5', name: 'PostgreSQL', level: 'EXPERT', category: 'Databases' },
  { id: '6', name: 'Redis', level: 'ADVANCED', category: 'Caching' },
  { id: '7', name: 'AWS EKS/RDS/S3', level: 'ADVANCED', category: 'Cloud' },
  { id: '8', name: 'Docker & Kubernetes', level: 'ADVANCED', category: 'DevOps' },
];

const MOCK_EXPERIENCES = [
  { id: '1', title: 'Principal Software Architect', company: 'TechCorp Inc.', startDate: '2021-03', endDate: null, current: true, description: 'Led migration of monolith to 12 microservices on AWS EKS. Built AI-powered resume platform serving 500K users.' },
  { id: '2', title: 'Senior Backend Engineer', company: 'FinStack Ltd.', startDate: '2018-06', endDate: '2021-02', current: false, description: 'Designed event-sourced payment ledger with Kafka and PostgreSQL. Reduced latency by 60%.' },
];

const MOCK_EDUCATIONS = [
  { id: '1', institution: 'Stanford University', degree: 'B.Sc. Computer Science', fieldOfStudy: 'Distributed Systems', startYear: 2012, endYear: 2016, grade: '3.9 GPA' },
];

export const ProfilePage = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('personal');
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  // Personal
  const [personal, setPersonal] = useState(MOCK_PROFILE);
  // Skills
  const [skills, setSkills] = useState(MOCK_SKILLS);
  const [newSkill, setNewSkill] = useState({ name: '', level: 'INTERMEDIATE', category: 'General' });
  // Experience
  const [experiences, setExperiences] = useState(MOCK_EXPERIENCES);
  const [showExpForm, setShowExpForm] = useState(false);
  const [newExp, setNewExp] = useState({ title: '', company: '', startDate: '', endDate: '', current: false, description: '' });
  // Education
  const [educations, setEducations] = useState(MOCK_EDUCATIONS);
  const [showEduForm, setShowEduForm] = useState(false);
  const [newEdu, setNewEdu] = useState({ institution: '', degree: '', fieldOfStudy: '', startYear: '', endYear: '', grade: '' });
  // Resume
  const [resumes, setResumes] = useState([]);
  const resumeRef = useRef(null);

  // Load profile from API
  useEffect(() => {
    const load = async () => {
      try {
        const [p, s, e, edu, r] = await Promise.allSettled([
          profileService.getMyProfile(),
          profileService.getSkills(),
          profileService.getExperiences(),
          profileService.getEducations(),
          profileService.getResumes(),
        ]);
        if (p.status === 'fulfilled' && p.value) setPersonal({ ...MOCK_PROFILE, ...(p.value?.data || p.value) });
        if (s.status === 'fulfilled' && Array.isArray(s.value?.data || s.value)) setSkills(s.value?.data || s.value);
        if (e.status === 'fulfilled' && Array.isArray(e.value?.data || e.value)) setExperiences(e.value?.data || e.value);
        if (edu.status === 'fulfilled' && Array.isArray(edu.value?.data || edu.value)) setEducations(edu.value?.data || edu.value);
        if (r.status === 'fulfilled' && Array.isArray(r.value?.data || r.value)) setResumes(r.value?.data || r.value);
      } catch { /* use mock data */ }
    };
    load();
  }, []);

  const handleSave = async () => {
    setSaving(true);
    try { await profileService.updateProfile(personal); } catch { /* offline */ }
    setSaving(false); setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const addSkill = async () => {
    if (!newSkill.name.trim()) return;
    const skill = { ...newSkill, id: `local-${Date.now()}` };
    setSkills(prev => [...prev, skill]);
    setNewSkill({ name: '', level: 'INTERMEDIATE', category: 'General' });
    try { await profileService.addSkill(newSkill); } catch { /* offline */ }
  };

  const removeSkill = async (id) => {
    setSkills(prev => prev.filter(s => s.id !== id));
    try { await profileService.deleteSkill(id); } catch { /* offline */ }
  };

  const addExperience = async () => {
    const exp = { ...newExp, id: `local-${Date.now()}` };
    setExperiences(prev => [...prev, exp]);
    setNewExp({ title: '', company: '', startDate: '', endDate: '', current: false, description: '' });
    setShowExpForm(false);
    try { await profileService.addExperience(newExp); } catch { /* offline */ }
  };

  const removeExperience = async (id) => {
    setExperiences(prev => prev.filter(e => e.id !== id));
    try { await profileService.deleteExperience(id); } catch { /* offline */ }
  };

  const addEducation = async () => {
    const edu = { ...newEdu, id: `local-${Date.now()}` };
    setEducations(prev => [...prev, edu]);
    setNewEdu({ institution: '', degree: '', fieldOfStudy: '', startYear: '', endYear: '', grade: '' });
    setShowEduForm(false);
    try { await profileService.addEducation(newEdu); } catch { /* offline */ }
  };

  const handleResumeUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    const local = { id: `local-${Date.now()}`, fileName: file.name, fileSize: file.size, uploadedAt: new Date().toISOString() };
    setResumes(prev => [...prev, local]);
    try {
      const uploaded = await profileService.uploadResume(file);
      if (uploaded?.id) setResumes(prev => prev.map(r => r.id === local.id ? uploaded : r));
    } catch { /* offline */ }
  };

  const displayInitial = (personal.firstName || user?.name || 'U')[0].toUpperCase();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>

      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800 }}>My Profile</h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            Resume data · Skills · Experience · Stored in Profile Service (S3 + PostgreSQL)
          </p>
        </div>
        <button onClick={handleSave} disabled={saving} className="btn-primary">
          {saving ? <><Loader size={16} style={{ animation: 'spin 1s linear infinite' }} /><span>Saving…</span></>
            : saved ? <><CheckCircle size={16} /><span>Saved!</span></>
            : <><Save size={16} /><span>Save Profile</span></>
          }
        </button>
      </div>

      {/* Tab Nav */}
      <div style={{ display: 'flex', gap: '4px', background: 'rgba(255,255,255,0.04)', padding: '4px', borderRadius: 'var(--radius-md)', width: 'fit-content' }}>
        {TABS.map(tab => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.id;
          return (
            <button key={tab.id} onClick={() => setActiveTab(tab.id)}
              style={{
                padding: '10px 18px', borderRadius: 'calc(var(--radius-md) - 2px)',
                border: 'none', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px',
                background: isActive ? 'var(--gradient-main)' : 'transparent',
                color: isActive ? '#fff' : 'var(--text-secondary)',
                fontWeight: isActive ? 700 : 500, fontSize: '0.875rem',
                transition: 'all 0.2s', boxShadow: isActive ? '0 4px 14px rgba(99,102,241,0.4)' : 'none'
              }}>
              <Icon size={16} /><span>{tab.label}</span>
            </button>
          );
        })}
      </div>

      {/* ─── Personal Info ─────────────────────────────────────────────────── */}
      {activeTab === 'personal' && (
        <div className="glass-card" style={{ padding: '32px' }}>
          {/* Avatar */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '24px', marginBottom: '32px' }}>
            <div style={{ position: 'relative' }}>
              <div style={{
                width: '88px', height: '88px', borderRadius: '50%', background: 'var(--gradient-main)',
                display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff',
                fontSize: '2.5rem', fontWeight: 800, boxShadow: '0 8px 24px rgba(99,102,241,0.4)'
              }}>{displayInitial}</div>
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '4px' }}>Profile Picture</h3>
              <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '10px' }}>JPG, PNG up to 5MB. Stored in AWS S3.</p>
              <button className="btn-secondary" style={{ fontSize: '0.8rem', padding: '8px 14px' }}>
                <Upload size={14} /><span>Upload Avatar</span>
              </button>
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '18px' }}>
            {[
              { key: 'firstName', label: 'First Name', icon: User },
              { key: 'lastName', label: 'Last Name', icon: User },
            ].map(f => (
              <div className="form-group" key={f.key} style={{ margin: 0 }}>
                <label className="form-label">{f.label}</label>
                <input type="text" className="form-input" value={personal[f.key] || ''}
                  onChange={e => setPersonal(p => ({ ...p, [f.key]: e.target.value }))} />
              </div>
            ))}

            <div className="form-group" style={{ gridColumn: 'span 2', margin: 0 }}>
              <label className="form-label">Professional Headline</label>
              <input type="text" className="form-input" value={personal.headline || ''}
                onChange={e => setPersonal(p => ({ ...p, headline: e.target.value }))}
                placeholder="e.g. Senior Software Architect | Java 21 & AI Systems" />
            </div>

            <div className="form-group" style={{ gridColumn: 'span 2', margin: 0 }}>
              <label className="form-label">Bio / Professional Summary</label>
              <textarea className="form-input" rows={4} value={personal.bio || ''}
                onChange={e => setPersonal(p => ({ ...p, bio: e.target.value }))} style={{ resize: 'vertical' }} />
            </div>

            {[
              { key: 'phone', label: 'Phone', icon: Phone, ph: '+1 (555) 234-5678' },
              { key: 'location', label: 'Location', icon: MapPin, ph: 'San Francisco, CA' },
              { key: 'github', label: 'GitHub URL', icon: Github, ph: 'https://github.com/...' },
              { key: 'linkedin', label: 'LinkedIn URL', icon: Linkedin, ph: 'https://linkedin.com/in/...' },
            ].map(f => (
              <div className="form-group" key={f.key} style={{ margin: 0 }}>
                <label className="form-label">{f.label}</label>
                <div style={{ position: 'relative' }}>
                  <f.icon size={15} style={{ position: 'absolute', left: '12px', top: '14px', color: 'var(--text-muted)' }} />
                  <input type="text" className="form-input" style={{ paddingLeft: '38px' }}
                    value={personal[f.key] || ''} placeholder={f.ph}
                    onChange={e => setPersonal(p => ({ ...p, [f.key]: e.target.value }))} />
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* ─── Skills ────────────────────────────────────────────────────────── */}
      {activeTab === 'skills' && (
        <div className="glass-card" style={{ padding: '28px' }}>
          <h3 style={{ fontWeight: 700, marginBottom: '20px' }}>Technical Skills & Competencies</h3>

          {/* Add Skill */}
          <div style={{ display: 'flex', gap: '12px', marginBottom: '24px', flexWrap: 'wrap' }}>
            <input type="text" className="form-input" style={{ flex: 2, minWidth: '180px' }}
              placeholder="Skill name (e.g. Docker, Kafka, React…)"
              value={newSkill.name} onChange={e => setNewSkill(p => ({ ...p, name: e.target.value }))}
              onKeyDown={e => e.key === 'Enter' && addSkill()} />
            <select className="form-select" style={{ flex: 1, minWidth: '130px' }}
              value={newSkill.level} onChange={e => setNewSkill(p => ({ ...p, level: e.target.value }))}>
              {['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'].map(l => <option key={l} value={l}>{l}</option>)}
            </select>
            <input type="text" className="form-input" style={{ flex: 1, minWidth: '120px' }}
              placeholder="Category" value={newSkill.category}
              onChange={e => setNewSkill(p => ({ ...p, category: e.target.value }))} />
            <button onClick={addSkill} className="btn-primary" style={{ flexShrink: 0 }}>
              <Plus size={16} /><span>Add</span>
            </button>
          </div>

          {/* Group skills by category */}
          {Array.from(new Set(skills.map(s => s.category))).map(cat => (
            <div key={cat} style={{ marginBottom: '20px' }}>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '10px' }}>{cat}</div>
              <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                {skills.filter(s => s.category === cat).map(skill => (
                  <div key={skill.id} style={{
                    display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 14px',
                    borderRadius: 'var(--radius-md)', background: 'rgba(255,255,255,0.04)',
                    border: `1px solid ${LEVEL_COLORS[skill.level]}30`
                  }}>
                    <span style={{ fontWeight: 600, fontSize: '0.875rem' }}>{skill.name}</span>
                    <span style={{ fontSize: '0.68rem', fontWeight: 700, color: LEVEL_COLORS[skill.level] }}>{skill.level}</span>
                    <button onClick={() => removeSkill(skill.id)}
                      style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)', padding: '0 2px', display: 'flex' }}>
                      <Trash2 size={12} />
                    </button>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}

      {/* ─── Experience ────────────────────────────────────────────────────── */}
      {activeTab === 'experience' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button onClick={() => setShowExpForm(!showExpForm)} className="btn-primary">
              <Plus size={16} /><span>Add Experience</span>
            </button>
          </div>

          {showExpForm && (
            <div className="glass-card" style={{ padding: '24px', border: '1px solid rgba(99,102,241,0.3)' }}>
              <h4 style={{ fontWeight: 700, marginBottom: '16px' }}>New Experience</h4>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px' }}>
                {[
                  { key: 'title', label: 'Job Title', ph: 'Senior Engineer' },
                  { key: 'company', label: 'Company', ph: 'Google' },
                  { key: 'startDate', label: 'Start Date', type: 'month' },
                  { key: 'endDate', label: 'End Date', type: 'month' },
                ].map(f => (
                  <div className="form-group" key={f.key} style={{ margin: 0 }}>
                    <label className="form-label">{f.label}</label>
                    <input type={f.type || 'text'} className="form-input" placeholder={f.ph}
                      value={newExp[f.key]} onChange={e => setNewExp(p => ({ ...p, [f.key]: e.target.value }))} />
                  </div>
                ))}
                <div className="form-group" style={{ gridColumn: 'span 2', margin: 0 }}>
                  <label className="form-label">Description</label>
                  <textarea className="form-input" rows={3} value={newExp.description}
                    onChange={e => setNewExp(p => ({ ...p, description: e.target.value }))}
                    placeholder="Key achievements and responsibilities..." style={{ resize: 'vertical' }} />
                </div>
              </div>
              <div style={{ display: 'flex', gap: '10px', marginTop: '14px' }}>
                <button onClick={() => setShowExpForm(false)} className="btn-secondary">Cancel</button>
                <button onClick={addExperience} className="btn-primary"><Save size={16} /><span>Save</span></button>
              </div>
            </div>
          )}

          {experiences.map(exp => (
            <div key={exp.id} className="glass-card" style={{ padding: '22px', borderLeft: '4px solid var(--accent-primary)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div>
                  <h3 style={{ fontWeight: 700, fontSize: '1.05rem' }}>{exp.title}</h3>
                  <div style={{ color: 'var(--accent-primary)', fontWeight: 600, fontSize: '0.9rem' }}>{exp.company}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginTop: '2px' }}>
                    {exp.startDate} — {exp.current ? 'Present' : exp.endDate}
                    {exp.current && <span className="badge badge-green" style={{ marginLeft: '8px', fontSize: '0.68rem' }}>Current</span>}
                  </div>
                </div>
                <button onClick={() => removeExperience(exp.id)}
                  style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}>
                  <Trash2 size={16} />
                </button>
              </div>
              {exp.description && (
                <p style={{ marginTop: '12px', fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: 1.6 }}>
                  {exp.description}
                </p>
              )}
            </div>
          ))}
        </div>
      )}

      {/* ─── Education ─────────────────────────────────────────────────────── */}
      {activeTab === 'education' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button onClick={() => setShowEduForm(!showEduForm)} className="btn-primary">
              <Plus size={16} /><span>Add Education</span>
            </button>
          </div>

          {showEduForm && (
            <div className="glass-card" style={{ padding: '24px', border: '1px solid rgba(99,102,241,0.3)' }}>
              <h4 style={{ fontWeight: 700, marginBottom: '16px' }}>Add Education</h4>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px' }}>
                {[
                  { key: 'institution', label: 'Institution', ph: 'Stanford University' },
                  { key: 'degree', label: 'Degree', ph: 'B.Sc. Computer Science' },
                  { key: 'fieldOfStudy', label: 'Field of Study', ph: 'Distributed Systems' },
                  { key: 'grade', label: 'Grade / GPA', ph: '3.9 GPA' },
                  { key: 'startYear', label: 'Start Year', ph: '2012' },
                  { key: 'endYear', label: 'End Year', ph: '2016' },
                ].map(f => (
                  <div className="form-group" key={f.key} style={{ margin: 0 }}>
                    <label className="form-label">{f.label}</label>
                    <input type="text" className="form-input" placeholder={f.ph}
                      value={newEdu[f.key]} onChange={e => setNewEdu(p => ({ ...p, [f.key]: e.target.value }))} />
                  </div>
                ))}
              </div>
              <div style={{ display: 'flex', gap: '10px', marginTop: '14px' }}>
                <button onClick={() => setShowEduForm(false)} className="btn-secondary">Cancel</button>
                <button onClick={addEducation} className="btn-primary"><Save size={16} /><span>Save</span></button>
              </div>
            </div>
          )}

          {educations.map(edu => (
            <div key={edu.id} className="glass-card" style={{ padding: '22px', borderLeft: '4px solid var(--accent-cyan)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div>
                  <h3 style={{ fontWeight: 700 }}>{edu.degree}</h3>
                  <div style={{ color: 'var(--accent-cyan)', fontWeight: 600, fontSize: '0.9rem' }}>{edu.institution}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginTop: '2px' }}>
                    {edu.fieldOfStudy} · {edu.startYear}–{edu.endYear}
                    {edu.grade && <span style={{ marginLeft: '8px', color: 'var(--accent-green)', fontWeight: 600 }}>{edu.grade}</span>}
                  </div>
                </div>
                <button onClick={() => setEducations(prev => prev.filter(e => e.id !== edu.id))}
                  style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}>
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* ─── Resumes ───────────────────────────────────────────────────────── */}
      {activeTab === 'resume' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {/* Upload zone */}
          <div className="glass-card" onClick={() => resumeRef.current?.click()}
            style={{
              padding: '40px', textAlign: 'center', cursor: 'pointer',
              border: '2px dashed rgba(99,102,241,0.4)', borderRadius: 'var(--radius-xl)',
              background: 'rgba(99,102,241,0.04)', transition: 'all 0.2s'
            }}>
            <input ref={resumeRef} type="file" accept=".pdf,.doc,.docx" hidden onChange={handleResumeUpload} />
            <Upload size={36} color="var(--accent-primary)" style={{ marginBottom: '12px' }} />
            <h3 style={{ fontWeight: 700, marginBottom: '6px' }}>Upload Resume</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
              PDF, DOC, DOCX up to 10MB · Stored securely in AWS S3
            </p>
          </div>

          {resumes.length > 0 && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {resumes.map(r => (
                <div key={r.id} className="glass-card" style={{ padding: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
                    <div style={{ width: '40px', height: '40px', background: 'rgba(99,102,241,0.12)', borderRadius: '10px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                      <Award size={20} color="var(--accent-primary)" />
                    </div>
                    <div>
                      <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>{r.fileName}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                        {r.fileSize ? `${(r.fileSize / 1024).toFixed(1)} KB · ` : ''}
                        Uploaded {new Date(r.uploadedAt).toLocaleDateString()}
                      </div>
                    </div>
                  </div>
                  <button onClick={async () => { setResumes(prev => prev.filter(x => x.id !== r.id)); try { await profileService.deleteResume(r.id); } catch {} }}
                    style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--accent-red)' }}>
                    <Trash2 size={16} />
                  </button>
                </div>
              ))}
            </div>
          )}

          {resumes.length === 0 && (
            <div style={{ textAlign: 'center', padding: '20px', color: 'var(--text-muted)', fontSize: '0.875rem' }}>
              No resumes uploaded yet. Upload your resume to enable AI ATS analysis.
            </div>
          )}
        </div>
      )}
    </div>
  );
};
