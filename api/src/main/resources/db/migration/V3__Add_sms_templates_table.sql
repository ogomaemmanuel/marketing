CREATE TABLE IF NOT EXISTS sms_templates
(
    id uuid,
    content text,
    name character varying,
    version bigint,
    description text,
    created_at timestamp with time zone,
    updated_by character varying,
    created_by character varying,
    updated_at timestamp with time zone,
    PRIMARY KEY (id)
);