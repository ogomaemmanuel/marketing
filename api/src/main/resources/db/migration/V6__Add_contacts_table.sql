CREATE TABLE IF NOT EXISTS contacts
(
    id uuid,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    email character varying NOT NULL,
    created_at timestamp with time zone NOT NULL,
    version bigint,
    attributtes jsonb,
    created_by character varying,
    last_updated_at timestamp with time zone,
    last_updated_by character varying not null ,
    PRIMARY KEY (id)
);