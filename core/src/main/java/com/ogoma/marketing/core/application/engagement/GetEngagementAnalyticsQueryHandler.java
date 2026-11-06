package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;
import org.jspecify.annotations.NonNull;

public record GetEngagementAnalyticsQueryHandler(
        CampaignEngagementRepository campaignEngagementRepository
) implements QueryHandler<GetEngagementAnalyticsQuery, GetEngagementAnalyticsView> {
    @Override
    public @NonNull Class<GetEngagementAnalyticsQuery> supports() {
        return GetEngagementAnalyticsQuery.class;
    }

    @Override
    public GetEngagementAnalyticsView handle(@NonNull GetEngagementAnalyticsQuery query) {
        return this.campaignEngagementRepository.getEngagementAnalytics();
    }
}
