-- Create the campaign_engagements table idempotently
CREATE TABLE IF NOT EXISTS campaign_engagements
(
    id              UUID PRIMARY KEY, -- Assuming CampaignEngagementID maps to a UUID primary key
    tracking_id     UUID        NOT NULL,
    engagement_type varchar(50) NOT NULL,
    received_at     TIMESTAMPTZ NOT NULL,
    user_agent      TEXT,
    destination_url TEXT,
    version         bigint      null     default 0,
    ip_address      VARCHAR(45),      -- Supports both IPv4 and IPv6 lengths
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create performance indexes idempotently
CREATE INDEX IF NOT EXISTS idx_campaign_engagements_tracking_id
    ON campaign_engagements (tracking_id);

CREATE INDEX IF NOT EXISTS idx_campaign_engagements_received_at
    ON campaign_engagements (received_at);