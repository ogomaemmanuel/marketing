CREATE TABLE IF NOT EXISTS campaign_audience
(
    campaign_id uuid NOT NULL REFERENCES campaigns (id),
    audience_id uuid NOT NULL REFERENCES audiences (id),
    PRIMARY KEY (campaign_id, audience_id)
);