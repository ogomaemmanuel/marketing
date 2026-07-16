ALTER TABLE IF EXISTS campaigns
    ADD COLUMN IF NOT EXISTS channels character varying[];