ALTER TABLE IF EXISTS campaigns
    ADD COLUMN IF NOT EXISTS sms_template_id uuid;