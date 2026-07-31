-- =========================================================
-- Flyway Database Migration: V1__init_job_schema.sql
-- Production Schema for CareerOS Job Service
-- PostgreSQL 16 / Java 21 / Spring Boot 3
-- =========================================================

-- 1. Job Postings Table
CREATE TABLE job_postings (
    id UUID PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(100),
    employment_type VARCHAR(30) NOT NULL,
    min_salary NUMERIC(15, 2),
    max_salary NUMERIC(15, 2),
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    posted_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Required Skills Element Collection Table
CREATE TABLE job_required_skills (
    job_id UUID NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    skill_name VARCHAR(100) NOT NULL
);

-- 2. Job Applications Table
CREATE TABLE job_applications (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    candidate_id UUID NOT NULL,
    cover_letter_text TEXT,
    resume_url VARCHAR(500),
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_job_candidate UNIQUE (job_id, candidate_id)
);

-- Performance & Index Optimizations
CREATE INDEX idx_job_company ON job_postings(company_name);
CREATE INDEX idx_job_status ON job_postings(status);
CREATE INDEX idx_job_title ON job_postings(title);

CREATE INDEX idx_app_candidate ON job_applications(candidate_id, created_at DESC);
CREATE INDEX idx_app_job ON job_applications(job_id, created_at DESC);
CREATE INDEX idx_app_status ON job_applications(status);
