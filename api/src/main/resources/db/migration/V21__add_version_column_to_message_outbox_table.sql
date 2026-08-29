ALTER TABLE IF EXISTS message_outbox
    ADD COLUMN IF NOT EXISTS version bigint NOT NULL;