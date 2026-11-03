-- 2. Create the campaign_message_status table if it doesn't exist
CREATE TABLE IF NOT EXISTS campaign_message_status
(
    -- Primary key as UUID for CampaignMessageStatusID
    id              UUID PRIMARY KEY,

    -- Reference IDs as UUIDs
    campaign_id     UUID                     NOT NULL,
    contact_id      UUID                     NOT NULL,

    -- Channel (stored as string)
    channel         VARCHAR(50)              NOT NULL,

    -- Status using the PostgreSQL ENUM type created above
    status          VARCHAR(50)              NOT NULL DEFAULT 'IN_PROGRESS',

    -- Timestamps
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 3. Create indexes conditionally for query performance
CREATE INDEX IF NOT EXISTS idx_campaign_message_status_campaign_id
    ON campaign_message_status (campaign_id);

CREATE INDEX IF NOT EXISTS idx_campaign_message_status_contact_id
    ON campaign_message_status (contact_id);

CREATE INDEX IF NOT EXISTS idx_campaign_message_status_status
    ON campaign_message_status (status);