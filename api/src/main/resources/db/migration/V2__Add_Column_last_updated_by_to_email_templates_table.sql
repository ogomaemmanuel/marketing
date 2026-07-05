ALTER TABLE IF EXISTS email_templates
    ADD COLUMN IF NOT EXISTS last_updated_by character varying;