package com.ogoma.marketing.core.application.campaign.queries;

import com.ogoma.marketing.core.domain.campaigns.Channel;

import java.util.List;
import java.util.UUID;

public record GetCampaignByIDView(
        UUID id,
        String name,
        String description,
        String status,
        List<Channel> channels,
        List<TargetSegment> targetSegments,
        List<TargetAudience> targetAudiences

) {






}
