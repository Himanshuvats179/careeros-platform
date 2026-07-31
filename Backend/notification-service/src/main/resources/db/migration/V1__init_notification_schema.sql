-- =========================================================
-- Flyway Database Migration: V1__init_notification_schema.sql
-- Production Schema for CareerOS Notification Service
-- PostgreSQL 16 / Java 21 / Spring Boot 3 + RabbitMQ & Kafka
-- =========================================================

-- 1. Notification Logs Table
CREATE TABLE notification_logs (
    id UUID PRIMARY KEY,
    recipient_id UUID NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30) NOT NULL DEFAULT 'IN_APP',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    event_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Performance & Index Optimizations
CREATE INDEX idx_notif_recipient ON notification_logs(recipient_id, created_at DESC);
CREATE INDEX idx_notif_status ON notification_logs(status);
CREATE INDEX idx_notif_type ON notification_logs(type);
CREATE INDEX idx_notif_event ON notification_logs(event_id);
