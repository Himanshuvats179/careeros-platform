-- =============================================================================
-- CareerOS PostgreSQL Initialization Script
-- Creates separate databases for each microservice (database-per-service pattern)
-- =============================================================================

-- Auth Service Database
CREATE DATABASE careeros_auth;

-- Profile Service Database
CREATE DATABASE careeros_profile;

-- Notification Service Database
CREATE DATABASE careeros_notification;

-- Audit Service Database
CREATE DATABASE careeros_audit;

-- Job Service Database
CREATE DATABASE careeros_job;

-- Grant all to postgres user (in dev; use separate roles in production)
GRANT ALL PRIVILEGES ON DATABASE careeros_auth TO postgres;
GRANT ALL PRIVILEGES ON DATABASE careeros_profile TO postgres;
GRANT ALL PRIVILEGES ON DATABASE careeros_notification TO postgres;
GRANT ALL PRIVILEGES ON DATABASE careeros_audit TO postgres;
GRANT ALL PRIVILEGES ON DATABASE careeros_job TO postgres;
