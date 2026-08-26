CREATE  TABLE IF NOT EXISTS campaign_channels
(
    campaign_id uuid,
    channel character varying,
    primary key (campaign_id,channel),
    FOREIGN KEY (campaign_id)
        REFERENCES campaigns (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);