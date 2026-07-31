import React, { useState, useEffect, useCallback } from 'react';
import { auditService } from '../services/auditService';
import { Activity, Search, Download, Filter, ChevronLeft, ChevronRight, RefreshCw, CheckCircle, XCircle, AlertCircle, Clock } from 'lucide-react';

const SERVICE_COLORS = {
  AUTH_SERVICE:         { color: 'var(--accent-primary)', bg: 'rgba(99,102,241,0.12)' },
  PROFILE_SERVICE:      { color: 'var(--accent-cyan)',    bg: 'rgba(6,182,212,0.12)' },
  JOB_SERVICE:          { color: 'var(--accent-yellow)',  bg: 'rgba(245,158,11,0.12)' },
  NOTIFICATION_SERVICE: { color: 'var(--accent-pink)',    bg: 'rgba(236,72,153,0.12)' },
  AI_AGENT_SERVICE:     { color: 'var(--accent-green)',   bg: 'rgba(16,185,129,0.12)' },
  AUDIT_SERVICE:        { color: 'var(--text-muted)',     bg: 'rgba(255,255,255,0.06)' },
};

const STATUS_ICON = {
  SUCCESS: <CheckCircle size={16} color="var(--accent-green)" />,
  FAILURE: <XCircle size={16} color="var(--accent-red)" />,
  WARNING: <AlertCircle size={16} color="var(--accent-yellow)" />,
  PENDING: <Clock size={16} color="var(--text-muted)" />,
};

// Mock data for offline mode
const MOCK_LOGS = Array.from({ length: 24 }, (_, i) => ({
  id: `log-${i + 1}`,
  serviceName: Object.keys(SERVICE_COLORS)[i % 6],
  eventType: ['USER_LOGGED_IN', 'PROFILE_UPDATED', 'RESUME_UPLOADED', 'JOB_APPLIED', 'AI_QUERY', 'EMAIL_SENT'][i % 6],
  actorId: `user-00${(i % 3) + 1}`,
  status: ['SUCCESS', 'SUCCESS', 'SUCCESS', 'FAILURE', 'WARNING'][i % 5],
  ipAddress: `192.168.1.${10 + (i % 50)}`,
  timestamp: new Date(Date.now() - i * 3 * 60000).toISOString(),
  metadata: { detail: `Event detail for log ${i + 1}` }
}));

export const AuditLogsPage = () => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [serviceFilter, setServiceFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [expandedId, setExpandedId] = useState(null);
  const PAGE_SIZE = 10;

  const fetchLogs = useCallback(async () => {
    setLoading(true);
    try {
      const params = {
        page, size: PAGE_SIZE,
        ...(search && { search }),
        ...(serviceFilter && { serviceName: serviceFilter }),
        ...(statusFilter && { status: statusFilter }),
      };
      const response = await auditService.getLogs(params);
      const content = response?.content || response?.data?.content || response;
      if (Array.isArray(content)) {
        setLogs(content);
        setTotalPages(response?.totalPages || 1);
      } else {
        throw new Error('unexpected response');
      }
    } catch {
      // Mock fallback
      const filtered = MOCK_LOGS.filter(l =>
        (!search || l.eventType.includes(search.toUpperCase()) || l.serviceName.includes(search.toUpperCase())) &&
        (!serviceFilter || l.serviceName === serviceFilter) &&
        (!statusFilter || l.status === statusFilter)
      );
      const start = page * PAGE_SIZE;
      setLogs(filtered.slice(start, start + PAGE_SIZE));
      setTotalPages(Math.ceil(filtered.length / PAGE_SIZE));
    } finally {
      setLoading(false);
    }
  }, [page, search, serviceFilter, statusFilter]);

  useEffect(() => { fetchLogs(); }, [fetchLogs]);

  const handleExport = async () => {
    try {
      const blob = await auditService.exportLogs({ serviceName: serviceFilter, status: statusFilter });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url; a.download = `careeros-audit-logs-${Date.now()}.csv`;
      a.click(); URL.revokeObjectURL(url);
    } catch {
      alert('Export unavailable in offline mode.');
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Activity size={28} color="var(--accent-primary)" />
            System Audit Logs
          </h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            Real-time event stream from all CareerOS microservices
          </p>
        </div>
        <div style={{ display: 'flex', gap: '10px' }}>
          <button onClick={fetchLogs} className="btn-secondary">
            <RefreshCw size={16} /><span>Refresh</span>
          </button>
          <button onClick={handleExport} className="btn-secondary">
            <Download size={16} /><span>Export CSV</span>
          </button>
        </div>
      </div>

      {/* Filters */}
      <div className="glass-card" style={{ padding: '16px', display: 'flex', gap: '12px', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ position: 'relative', flex: 1, minWidth: '200px' }}>
          <Search size={16} style={{ position: 'absolute', left: '12px', top: '12px', color: 'var(--text-muted)' }} />
          <input type="text" className="form-input" style={{ paddingLeft: '38px' }}
            placeholder="Search events or services…"
            value={search} onChange={e => { setSearch(e.target.value); setPage(0); }} />
        </div>
        <select className="form-select" style={{ minWidth: '180px' }}
          value={serviceFilter} onChange={e => { setServiceFilter(e.target.value); setPage(0); }}>
          <option value="">All Services</option>
          {Object.keys(SERVICE_COLORS).map(s => <option key={s} value={s}>{s.replace(/_/g, ' ')}</option>)}
        </select>
        <select className="form-select" style={{ minWidth: '140px' }}
          value={statusFilter} onChange={e => { setStatusFilter(e.target.value); setPage(0); }}>
          <option value="">All Statuses</option>
          {['SUCCESS', 'FAILURE', 'WARNING', 'PENDING'].map(s => <option key={s} value={s}>{s}</option>)}
        </select>
        {(search || serviceFilter || statusFilter) && (
          <button onClick={() => { setSearch(''); setServiceFilter(''); setStatusFilter(''); setPage(0); }}
            className="btn-secondary" style={{ fontSize: '0.8rem' }}>
            Clear Filters
          </button>
        )}
      </div>

      {/* Log Table */}
      <div className="glass-card" style={{ overflow: 'hidden' }}>
        {/* Table Header */}
        <div style={{
          display: 'grid', gridTemplateColumns: '2fr 2fr 1fr 1.2fr 1fr 1.5fr',
          padding: '12px 20px', borderBottom: '1px solid var(--border-color)',
          fontSize: '0.78rem', color: 'var(--text-muted)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.05em'
        }}>
          <span>Event Type</span><span>Service</span><span>Status</span>
          <span>Actor</span><span>IP Address</span><span>Timestamp</span>
        </div>

        {loading ? (
          <div style={{ padding: '60px', textAlign: 'center' }}>
            <RefreshCw size={32} style={{ animation: 'spin 1s linear infinite', color: 'var(--accent-primary)' }} />
          </div>
        ) : logs.length === 0 ? (
          <div style={{ padding: '60px', textAlign: 'center', color: 'var(--text-muted)' }}>
            No audit logs found for the current filters.
          </div>
        ) : (
          logs.map((log, i) => {
            const svc = SERVICE_COLORS[log.serviceName] || SERVICE_COLORS.AUDIT_SERVICE;
            const isExpanded = expandedId === log.id;
            return (
              <div key={log.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                <div onClick={() => setExpandedId(isExpanded ? null : log.id)} style={{
                  display: 'grid', gridTemplateColumns: '2fr 2fr 1fr 1.2fr 1fr 1.5fr',
                  padding: '14px 20px', cursor: 'pointer', transition: 'background 0.15s',
                  background: isExpanded ? 'rgba(255,255,255,0.03)' : 'transparent',
                  ':hover': { background: 'rgba(255,255,255,0.02)' }
                }}>
                  <span style={{ fontSize: '0.875rem', fontWeight: 600, color: 'var(--text-primary)' }}>
                    {log.eventType}
                  </span>
                  <div>
                    <span style={{
                      fontSize: '0.75rem', fontWeight: 600, padding: '3px 8px', borderRadius: '6px',
                      background: svc.bg, color: svc.color
                    }}>
                      {log.serviceName?.replace(/_/g, ' ')}
                    </span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    {STATUS_ICON[log.status] || STATUS_ICON.PENDING}
                    <span style={{ fontSize: '0.8rem' }}>{log.status}</span>
                  </div>
                  <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', fontFamily: 'monospace' }}>
                    {log.actorId?.slice(0, 12)}…
                  </span>
                  <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontFamily: 'monospace' }}>
                    {log.ipAddress}
                  </span>
                  <span style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>
                    {new Date(log.timestamp).toLocaleString()}
                  </span>
                </div>

                {/* Expanded metadata */}
                {isExpanded && (
                  <div style={{
                    padding: '12px 20px 16px 20px', background: 'rgba(0,0,0,0.2)',
                    borderTop: '1px solid var(--border-color)'
                  }}>
                    <pre style={{
                      fontSize: '0.8rem', color: 'var(--text-secondary)', fontFamily: 'monospace',
                      overflow: 'auto', whiteSpace: 'pre-wrap'
                    }}>
                      {JSON.stringify(log.metadata || { id: log.id, eventType: log.eventType }, null, 2)}
                    </pre>
                  </div>
                )}
              </div>
            );
          })
        )}

        {/* Pagination */}
        <div style={{ padding: '14px 20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderTop: '1px solid var(--border-color)' }}>
          <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
            Page {page + 1} of {totalPages}
          </span>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button onClick={() => setPage(p => Math.max(0, p - 1))} disabled={page === 0}
              className="btn-secondary" style={{ padding: '8px 12px' }}>
              <ChevronLeft size={16} />
            </button>
            <button onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))} disabled={page >= totalPages - 1}
              className="btn-secondary" style={{ padding: '8px 12px' }}>
              <ChevronRight size={16} />
            </button>
          </div>
        </div>
      </div>

      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>
    </div>
  );
};
