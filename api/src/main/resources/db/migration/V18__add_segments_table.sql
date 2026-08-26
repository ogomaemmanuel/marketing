CREATE TABLE IF NOT EXISTS segments
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255)             NOT NULL,
    ruleset         JSONB                    NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by      VARCHAR(255)             NOT NULL,
    last_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_by VARCHAR(255)             NOT NULL,
    version         BIGINT                   NOT NULL DEFAULT 0
);