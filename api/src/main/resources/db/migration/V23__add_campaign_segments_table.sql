CREATE TABLE IF NOT EXISTS marketing_main.campaign_segments
(
    campaign_id uuid NOT NULL  REFERENCES campaigns (id),
    segment_id uuid NOT NULL REFERENCES segments (id),
    PRIMARY KEY (campaign_id, segment_id)
)