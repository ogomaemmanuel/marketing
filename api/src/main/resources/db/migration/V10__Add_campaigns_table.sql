CREATE TABLE IF NOT EXISTS campaigns
(
    id              uuid,
    name            character varying NOT NULL,
    description     character varying,
    version         bigint,
    configuration   jsonb,
    created_by      character varying,
    created_at      timestamp with time zone,
    last_updated_at timestamp without time zone,
    last_updated_by character varying,
    status          character varying,
    PRIMARY KEY (id)
);