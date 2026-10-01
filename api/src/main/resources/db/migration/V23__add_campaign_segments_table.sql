CREATE TABLE IF NOT EXISTS campaign_segments
(
    campaign_id uuid NOT NULL  REFERENCES campaigns (id),
    segment_id uuid NOT NULL REFERENCES segments (id),
    PRIMARY KEY (campaign_id, segment_id)
)