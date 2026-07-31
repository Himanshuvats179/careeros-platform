-- =========================================================
-- Flyway Database Migration: V1__init_audit_schema.sql
-- Production Schema for CareerOS Audit Service
-- PostgreSQL 16 / Java 21 / Spring Boot 3 + Kafka
-- =========================================================

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    correlation_id VARCHAR(100),
    user_id UUID,
    event_type VARCHAR(100) NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    action VARCHAR(150) NOT NULL,
    request_data TEXT,
    response_data TEXT,
    ip_address VARCHAR(50),
    device VARCHAR(100),
    browser VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS',
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Future Scope Flags: ML Feature Pipeline & AWS CloudWatch Integration
    ml_feature_exported BOOLEAN NOT NULL DEFAULT FALSE,
    aws_cloudwatch_exported BOOLEAN NOT NULL DEFAULT FALSE
);

-- Performance & Query Optimization Indexes
CREATE UNIQUE INDEX idx_audit_event_id ON audit_logs(event_id);
CREATE INDEX idx_audit_user_id_timestamp ON audit_logs(user_id, timestamp DESC);
CREATE INDEX idx_audit_service_event ON audit_logs(service_name, event_type);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp DESC);
