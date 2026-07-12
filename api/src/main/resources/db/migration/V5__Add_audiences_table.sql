CREATE TABLE IF NOT EXISTS audiences
(
    id uuid,
    name character varying NOT NULL,
    version bigint NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    created_by character varying,
    updated_by character varying,
    PRIMARY KEY (id)
);