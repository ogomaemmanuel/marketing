CREATE TABLE IF NOT EXISTS marketing_main.users
(
    id          uuid,
    email       text,
    first_name  text,
    last_name   text,
    created_at  timestamp with time zone,
    updated_at  timestamp with time zone,
    version     bigint,
    external_id character varying,
    PRIMARY KEY (id)
);