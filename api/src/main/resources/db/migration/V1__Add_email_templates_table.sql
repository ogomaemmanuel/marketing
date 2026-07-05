CREATE TABLE IF NOT EXISTS email_templates
(
    id uuid NOT NULL,
    created_at timestamp with time zone,
    name character varying COLLATE pg_catalog."default",
    updated_at timestamp with time zone,
    email_template json,
    version bigint,
    created_by character varying COLLATE pg_catalog."default",
    last_updated_by character varying COLLATE pg_catalog."default",
    CONSTRAINT email_templates_pkey PRIMARY KEY (id)
)

