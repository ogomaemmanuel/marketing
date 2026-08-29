CREATE TABLE IF NOT EXISTS message_outbox
(
    id uuid,
    aggregate_type character varying NOT NULL,
    aggregate_id uuid NOT NULL,
    event_type character varying NOT NULL,
    event_version character varying NOT NULL,
    event_name character varying NOT NULL,
    payload jsonb NOT NULL,
    created_at timestamp with time zone NOT NULL,
    processed boolean NOT NULL DEFAULT false,
    processed_at timestamp with time zone,
    PRIMARY KEY (id)
);