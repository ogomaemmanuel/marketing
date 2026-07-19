package com.ogoma.marketing.core.application.campaign;

import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.Channel;

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
                campaignEntity.getChannels());
    }
}
