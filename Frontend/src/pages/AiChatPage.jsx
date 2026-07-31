import React, { useState, useRef, useEffect } from 'react';
import { aiService } from '../services/aiService';
import {
  Sparkles, Send, Loader, User, Bot, RotateCcw,
  Copy, CheckCheck, Briefcase, FileText, Map, Mic
} from 'lucide-react';

const QUICK_PROMPTS = [
  { icon: Briefcase, label: 'Career Roadmap', text: 'Create a 12-month roadmap for me to become a Staff Engineer at a FAANG company.' },
  { icon: FileText, label: 'Resume Review', text: 'Analyze my resume and give me 5 specific improvements to increase my ATS score.' },
  { icon: Map, label: 'Skill Gap', text: 'What skills am I missing to transition from Backend Developer to Solutions Architect?' },
  { icon: Mic, label: 'Interview Prep', text: 'Give me 5 system design interview questions for a Senior Engineer role at Google with model answers.' },
];

const MessageBubble = ({ message }) => {
  const [copied, setCopied] = useState(false);
  const isUser = message.role === 'user';

  const copyText = () => {
    navigator.clipboard.writeText(message.content);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div style={{
      display: 'flex', gap: '12px', flexDirection: isUser ? 'row-reverse' : 'row',
      marginBottom: '20px'
    }}>
      {/* Avatar */}
      <div style={{
        width: '36px', height: '36px', borderRadius: '50%', flexShrink: 0,
        background: isUser ? 'var(--gradient-main)' : 'rgba(99,102,241,0.15)',
        border: isUser ? 'none' : '1px solid rgba(99,102,241,0.3)',
        display: 'flex', alignItems: 'center', justifyContent: 'center'
      }}>
        {isUser ? <User size={18} color="#fff" /> : <Bot size={18} color="var(--accent-primary)" />}
      </div>

      {/* Bubble */}
      <div style={{ maxWidth: '72%', position: 'relative' }}>
        <div style={{
          padding: '14px 18px', borderRadius: isUser ? '18px 4px 18px 18px' : '4px 18px 18px 18px',
          background: isUser
            ? 'var(--gradient-main)'
            : 'rgba(30,41,59,0.8)',
          border: isUser ? 'none' : '1px solid var(--border-color)',
          color: isUser ? '#fff' : 'var(--text-primary)',
          fontSize: '0.925rem', lineHeight: '1.7',
          whiteSpace: 'pre-wrap', wordBreak: 'break-word',
          boxShadow: isUser ? '0 4px 14px rgba(99,102,241,0.3)' : 'none'
        }}>
          {message.content}
        </div>
        {/* Copy button for AI messages */}
        {!isUser && (
          <button onClick={copyText} style={{
            position: 'absolute', top: '8px', right: '-32px',
            background: 'none', border: 'none', cursor: 'pointer',
            color: 'var(--text-muted)', padding: '4px'
          }}>
            {copied ? <CheckCheck size={14} color="var(--accent-green)" /> : <Copy size={14} />}
          </button>
        )}
        <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)', marginTop: '4px',
          textAlign: isUser ? 'right' : 'left' }}>
          {new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
        </div>
      </div>
    </div>
  );
};

const TypingIndicator = () => (
  <div style={{ display: 'flex', gap: '12px', marginBottom: '20px', alignItems: 'flex-end' }}>
    <div style={{
      width: '36px', height: '36px', borderRadius: '50%',
      background: 'rgba(99,102,241,0.15)', border: '1px solid rgba(99,102,241,0.3)',
      display: 'flex', alignItems: 'center', justifyContent: 'center'
    }}>
      <Bot size={18} color="var(--accent-primary)" />
    </div>
    <div style={{
      padding: '14px 20px', borderRadius: '4px 18px 18px 18px',
      background: 'rgba(30,41,59,0.8)', border: '1px solid var(--border-color)',
      display: 'flex', gap: '6px', alignItems: 'center'
    }}>
      {[0, 1, 2].map(i => (
        <div key={i} style={{
          width: '8px', height: '8px', borderRadius: '50%',
          background: 'var(--accent-primary)',
          animation: `bounce 1.4s ease-in-out ${i * 0.16}s infinite`
        }} />
      ))}
    </div>
  </div>
);

export const AiChatPage = () => {
  const [messages, setMessages] = useState([
    {
      id: 1, role: 'assistant', timestamp: Date.now(),
      content: `👋 Hello! I'm your **CareerOS AI Coach**, powered by GPT-4o.\n\nI can help you with:\n• 📄 Resume analysis & ATS optimization\n• 🗺️ Career roadmap planning\n• 🎯 Skill gap analysis\n• 🧠 Mock interview preparation\n• 💌 Cover letter generation\n\nWhat would you like to work on today?`
    }
  ]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const bottomRef = useRef(null);
  const textareaRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

  const sendMessage = async (text) => {
    const userText = text || input.trim();
    if (!userText || isLoading) return;

    const userMsg = { id: Date.now(), role: 'user', content: userText, timestamp: Date.now() };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setIsLoading(true);
    setError(null);

    // Build conversation history for context
    const history = messages.map(m => ({ role: m.role, content: m.content }));

    try {
      const response = await aiService.chat(userText, history);
      const aiContent = response?.data?.response || response?.response || response;
      const aiMsg = {
        id: Date.now() + 1, role: 'assistant',
        content: typeof aiContent === 'string' ? aiContent : JSON.stringify(aiContent),
        timestamp: Date.now()
      };
      setMessages(prev => [...prev, aiMsg]);
    } catch (err) {
      // Mock response when AI Agent is offline (dev mode)
      const mockResponse = `I'm currently in **offline mode** (AI Agent not reachable), but here's what I'd say:\n\n${getMockResponse(userText)}`;
      setMessages(prev => [...prev, {
        id: Date.now() + 1, role: 'assistant', content: mockResponse, timestamp: Date.now()
      }]);
    } finally {
      setIsLoading(false);
    }
  };

  const getMockResponse = (text) => {
    if (text.toLowerCase().includes('roadmap')) return '🗺️ For a Staff Engineer path:\n\n**Phase 1 (0–3 months):** Master system design fundamentals\n**Phase 2 (3–6 months):** Contribute to open source / build portfolio projects\n**Phase 3 (6–9 months):** Target L5/L6 interviews\n**Phase 4 (9–12 months):** Negotiate & onboard\n\nFocus on distributed systems, scalability, and leadership skills.';
    if (text.toLowerCase().includes('resume')) return '📄 Top 5 Resume Improvements:\n\n1. Quantify all achievements (e.g., "reduced latency by 40%")\n2. Add keywords from target job descriptions\n3. Move certifications to a dedicated section\n4. Lead with your strongest projects\n5. Use action verbs: Architected, Led, Reduced, Built';
    return '✨ Great question! As your AI Career Coach, I recommend focusing on your target role\'s requirements, building measurable achievements, and continuously upskilling in high-demand areas like cloud architecture, distributed systems, and AI/ML integration.';
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); sendMessage(); }
  };

  const clearChat = () => {
    setMessages([{
      id: Date.now(), role: 'assistant', timestamp: Date.now(),
      content: '🔄 Chat cleared! How can I help you with your career today?'
    }]);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: 'calc(100vh - 140px)', gap: '16px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Sparkles size={28} color="var(--accent-primary)" />
            AI Career Coach
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            Powered by GPT-4o · RAG-enhanced · Real-time career intelligence
          </p>
        </div>
        <button onClick={clearChat} className="btn-secondary">
          <RotateCcw size={16} />
          <span>Clear Chat</span>
        </button>
      </div>

      {/* Quick Prompts */}
      <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
        {QUICK_PROMPTS.map((p) => {
          const Icon = p.icon;
          return (
            <button key={p.label} onClick={() => sendMessage(p.text)} className="btn-secondary"
              style={{ fontSize: '0.8rem', padding: '8px 14px' }}>
              <Icon size={14} />
              <span>{p.label}</span>
            </button>
          );
        })}
      </div>

      {/* Messages */}
      <div className="glass-card" style={{
        flex: 1, overflowY: 'auto', padding: '24px',
        display: 'flex', flexDirection: 'column'
      }}>
        <style>{`
          @keyframes bounce {
            0%, 60%, 100% { transform: translateY(0); }
            30% { transform: translateY(-8px); }
          }
          @keyframes spin { to { transform: rotate(360deg); } }
        `}</style>

        {messages.map(msg => <MessageBubble key={msg.id} message={msg} />)}
        {isLoading && <TypingIndicator />}
        <div ref={bottomRef} />
      </div>

      {/* Input */}
      <div className="glass-card" style={{ padding: '16px', display: 'flex', gap: '12px', alignItems: 'flex-end' }}>
        <textarea
          ref={textareaRef}
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Ask your AI coach anything… (Shift+Enter for new line)"
          rows={2}
          style={{
            flex: 1, background: 'transparent', border: '1px solid var(--border-color)',
            borderRadius: 'var(--radius-md)', padding: '12px 16px', color: 'var(--text-primary)',
            fontSize: '0.95rem', resize: 'none', outline: 'none', fontFamily: 'inherit',
            lineHeight: '1.6'
          }}
          onFocus={e => e.target.style.borderColor = 'var(--accent-primary)'}
          onBlur={e => e.target.style.borderColor = 'var(--border-color)'}
        />
        <button onClick={() => sendMessage()} disabled={!input.trim() || isLoading}
          className="btn-primary" style={{
            padding: '13px 20px', flexShrink: 0,
            opacity: (!input.trim() || isLoading) ? 0.5 : 1,
            cursor: (!input.trim() || isLoading) ? 'not-allowed' : 'pointer'
          }}>
          {isLoading ? <Loader size={20} style={{ animation: 'spin 1s linear infinite' }} /> : <Send size={20} />}
        </button>
      </div>
    </div>
  );
};
