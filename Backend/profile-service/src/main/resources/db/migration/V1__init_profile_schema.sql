-- =========================================================
-- Flyway Database Migration: V1__init_profile_schema.sql
-- Production Schema for CareerOS Profile Service
-- PostgreSQL 16 / Java 21 / Spring Boot 3
-- =========================================================

-- 1. Profiles Table
CREATE TABLE profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    headline VARCHAR(150),
    bio TEXT,
    phone VARCHAR(30),
    location VARCHAR(150),
    website_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    profile_picture_url VARCHAR(500),
    resume_url VARCHAR(500),
    
    -- Audit & Soft Delete Fields
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

-- Indexes for Profiles
CREATE UNIQUE INDEX idx_profiles_user_id ON profiles(user_id);
CREATE INDEX idx_profiles_location ON profiles(location);
CREATE INDEX idx_profiles_headline ON profiles(headline);
CREATE INDEX idx_profiles_is_deleted ON profiles(is_deleted);

-- 2. Skills Table
CREATE TABLE skills (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    proficiency_level VARCHAR(30) NOT NULL,
    category VARCHAR(50),
    years_of_experience INT,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_skills_profile_id ON skills(profile_id);
CREATE INDEX idx_skills_name ON skills(name);

-- 3. Experiences Table
CREATE TABLE experiences (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    company_name VARCHAR(150) NOT NULL,
    title VARCHAR(150) NOT NULL,
    location VARCHAR(150),
    employment_type VARCHAR(30) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_current_role BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_experiences_profile_id ON experiences(profile_id);
CREATE INDEX idx_experiences_company_name ON experiences(company_name);

-- 4. Educations Table
CREATE TABLE educations (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    institution VARCHAR(150) NOT NULL,
    degree_type VARCHAR(30) NOT NULL,
    field_of_study VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    grade VARCHAR(20),
    description TEXT,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_educations_profile_id ON educations(profile_id);
CREATE INDEX idx_educations_institution ON educations(institution);

-- 5. Projects Table
CREATE TABLE projects (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    project_url VARCHAR(255),
    github_url VARCHAR(255),
    technologies_used VARCHAR(300),
    start_date DATE,
    end_date DATE,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_projects_profile_id ON projects(profile_id);

-- 6. Certifications Table
CREATE TABLE certifications (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    issuing_organization VARCHAR(150) NOT NULL,
    issue_date DATE NOT NULL,
    expiration_date DATE,
    credential_id VARCHAR(100),
    credential_url VARCHAR(255),
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_certifications_profile_id ON certifications(profile_id);

-- 7. Languages Table
CREATE TABLE languages (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    language_name VARCHAR(100) NOT NULL,
    proficiency_level VARCHAR(30) NOT NULL,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_languages_profile_id ON languages(profile_id);
