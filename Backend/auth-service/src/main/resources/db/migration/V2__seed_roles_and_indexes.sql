-- =========================================================
-- Flyway Migration: V2__seed_roles_and_indexes.sql
-- Adds role seeding and additional performance indexes
-- Runs AFTER V1__init_auth_schema.sql
-- =========================================================

-- Seed default roles (no-op if already inserted)
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER'
FROM users
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = users.id AND ur.role = 'ROLE_USER'
)
LIMIT 0; -- placeholder, real seed happens at app startup via InitialDataLoader

-- Additional performance indexes
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_expiry ON refresh_tokens(expiry_date);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
