ALTER TABLE notifications
    ADD COLUMN correlation_id VARCHAR(100);

UPDATE notifications
SET correlation_id = 'SYSTEM_MIGRATION'
WHERE correlation_id IS NULL;

ALTER TABLE notifications
    ALTER COLUMN correlation_id SET NOT NULL;

ALTER TABLE notifications
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

CREATE INDEX idx_notifications_correlation
    ON notifications(correlation_id);