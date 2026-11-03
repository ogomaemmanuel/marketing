package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;

public record GetEngagementAnalyticsQueryHandler(
        CampaignEngagementRepository campaignEngagementRepository
) implements QueryHandler<GetEngagementAnalyticsQuery, GetEngagementAnalyticsView> {
    @Override
    public Class<GetEngagementAnalyticsQuery> supports() {
        return GetEngagementAnalyticsQuery.class;
    }

    @Override
    public GetEngagementAnalyticsView handle(GetEngagementAnalyticsQuery query) {
        return this.campaignEngagementRepository.getEngagementAnalytics();
    }
}
