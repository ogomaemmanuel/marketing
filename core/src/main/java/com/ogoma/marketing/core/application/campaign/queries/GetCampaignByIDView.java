package com.ogoma.marketing.core.application.campaign.queries;

import com.ogoma.marketing.core.domain.campaigns.CampaignConfiguration;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.Channel;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record GetCampaignByIDView(
        UUID id,
        String name,
        String description,
        Set<Channel> channels
) {

    public GetCampaignByIDView(CampaignEntity campaignEntity) {
        this(
                campaignEntity.getId().id(),
                campaignEntity.getName(),
                campaignEntity.getDescription(),
                Optional.ofNullable(campaignEntity.getCampaignConfiguration()).map(CampaignConfiguration::channels).orElse(null));
    }
}
