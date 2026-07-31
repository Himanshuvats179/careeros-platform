-- ============================================================
-- Flyway Migration V1: Init Job & Application Service Schema
-- ============================================================

CREATE TABLE IF NOT EXISTS job_postings (
    id VARCHAR(64) PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    responsibilities TEXT,
    requirements TEXT,
    min_salary NUMERIC(12, 2),
    max_salary NUMERIC(12, 2),
    employment_type VARCHAR(50) NOT NULL DEFAULT 'FULL_TIME',
    experience_level VARCHAR(50) NOT NULL DEFAULT 'MID_LEVEL',
    industry VARCHAR(100),
    location VARCHAR(150) NOT NULL,
    is_remote BOOLEAN NOT NULL DEFAULT FALSE,
    is_hybrid BOOLEAN NOT NULL DEFAULT FALSE,
    benefits TEXT,
    application_deadline TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    views_count INT NOT NULL DEFAULT 0,
    applications_count INT NOT NULL DEFAULT 0,
    created_by VARCHAR(64),
    last_modified_by VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS job_required_skills (
    job_id VARCHAR(64) NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    skill_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (job_id, skill_name)
);

CREATE TABLE IF NOT EXISTS job_applications (
    id VARCHAR(64) PRIMARY KEY,
    job_id VARCHAR(64) NOT NULL REFERENCES job_postings(id),
    candidate_id VARCHAR(64) NOT NULL,
    candidate_name VARCHAR(150),
    candidate_email VARCHAR(150),
    status VARCHAR(50) NOT NULL DEFAULT 'APPLIED',
    resume_url TEXT,
    cover_letter TEXT,
    ats_score INT,
    ai_assisted BOOLEAN NOT NULL DEFAULT FALSE,
    ai_match_explanation TEXT,
    notes TEXT,
    applied_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_candidate_job UNIQUE (candidate_id, job_id)
);

CREATE TABLE IF NOT EXISTS job_bookmarks (
    candidate_id VARCHAR(64) NOT NULL,
    job_id VARCHAR(64) NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (candidate_id, job_id)
);

CREATE TABLE IF NOT EXISTS recent_job_views (
    id VARCHAR(64) PRIMARY KEY,
    candidate_id VARCHAR(64) NOT NULL,
    job_id VARCHAR(64) NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    viewed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- B-tree and GIN Indexes for High-Performance Searching
CREATE INDEX IF NOT EXISTS idx_job_company ON job_postings(company_name);
CREATE INDEX IF NOT EXISTS idx_job_location ON job_postings(location);
CREATE INDEX IF NOT EXISTS idx_job_status ON job_postings(status);
CREATE INDEX IF NOT EXISTS idx_job_salary ON job_postings(min_salary, max_salary);
CREATE INDEX IF NOT EXISTS idx_job_created ON job_postings(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_job_skills_name ON job_required_skills(skill_name);
CREATE INDEX IF NOT EXISTS idx_app_candidate ON job_applications(candidate_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_app_job ON job_applications(job_id, status);
