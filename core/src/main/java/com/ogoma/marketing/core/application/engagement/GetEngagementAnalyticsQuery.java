package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;

import java.util.Set;

public record GetEngagementAnalyticsQuery(
        Set<CampaignID> campaignIDS
) implements Query<GetEngagementAnalyticsView> {
    public GetEngagementAnalyticsQuery {
        campaignIDS = campaignIDS == null ? Set.of() : Set.copyOf(campaignIDS);
    }
}
