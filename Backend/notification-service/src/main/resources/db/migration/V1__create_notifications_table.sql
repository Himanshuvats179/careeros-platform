CREATE TABLE notifications
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    email VARCHAR(255) NOT NULL,

    type VARCHAR(50) NOT NULL,

    status VARCHAR(30) NOT NULL,

    subject VARCHAR(255) NOT NULL,

    body TEXT,

    provider VARCHAR(50),

    provider_message_id VARCHAR(255),

    retry_count INTEGER NOT NULL DEFAULT 0,

    error_message TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    sent_at TIMESTAMP
);

CREATE INDEX idx_notifications_user
    ON notifications(user_id);

CREATE INDEX idx_notifications_status
    ON notifications(status);

CREATE INDEX idx_notifications_type
    ON notifications(type);

CREATE INDEX idx_notifications_created
    ON notifications(created_at);