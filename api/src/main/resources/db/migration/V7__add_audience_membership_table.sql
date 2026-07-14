CREATE TABLE IF NOT EXISTS audience_membership
(
    id uuid,
    contact_id uuid NOT NULL,
    audience_id uuid NOT NULL,
    joined_at timestamp with time zone,
    version bigint,
    PRIMARY KEY (id)
);