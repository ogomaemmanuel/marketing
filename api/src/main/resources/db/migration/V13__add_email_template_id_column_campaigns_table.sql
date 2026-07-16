ALTER TABLE IF EXISTS campaigns
    ADD COLUMN IF NOT EXISTS email_template_id uuid;