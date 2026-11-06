package com.ogoma.marketing.api.campaigns;

import com.ogoma.marketing.core.application.engagement.GetEngagementAnalyticsQuery;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GetEngagementAnalyticsRequest(
        Set<UUID> campaignIDs
) {
    public GetEngagementAnalyticsRequest {
        campaignIDs = campaignIDs == null ? Set.of() : Set.copyOf(campaignIDs);
    }

    public GetEngagementAnalyticsQuery toQuery() {
        return new GetEngagementAnalyticsQuery(this.campaignIDs.stream().map(CampaignID::new).collect(Collectors.toSet()));
    }
}
