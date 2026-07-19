package com.ogoma.marketing.core.application.campaign.queries;

import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.Channel;

import java.util.Set;
import java.util.UUID;

public record GetCampaignsView(
        UUID id,
        String name,
        String description,
        Set<Channel> channels
) {
    public GetCampaignsView(CampaignEntity campaignEntity) {
        this(
                campaignEntity.getId().id(),
                campaignEntity.getName(),
                campaignEntity.getDescription(),
                campaignEntity.getChannels());
    }
}
