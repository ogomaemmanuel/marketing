ALTER TABLE IF EXISTS campaign_message_status
    ADD COLUMN IF NOT EXISTS version bigint not null default 0