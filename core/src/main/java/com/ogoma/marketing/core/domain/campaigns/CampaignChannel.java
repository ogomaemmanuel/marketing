package com.ogoma.marketing.core.domain.campaigns;


import org.springframework.data.relational.core.mapping.Table;

@Table("campaign_channels")
public record CampaignChannel(Channel channel) {}


