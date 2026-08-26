ALTER TABLE IF EXISTS segments
    ADD COLUMN IF NOT EXISTS description character varying;