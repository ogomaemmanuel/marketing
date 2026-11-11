package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;
import org.jspecify.annotations.NonNull;

import java.util.stream.Collectors;

public record GetEngagementAnalyticsQueryHandler(
        CampaignEngagementRepository campaignEngagementRepository
) implements QueryHandler<GetEngagementAnalyticsQuery, GetEngagementAnalyticsView> {
    @Override
    public @NonNull Class<GetEngagementAnalyticsQuery> supports() {
        return GetEngagementAnalyticsQuery.class;
    }

    @Override
    public GetEngagementAnalyticsView handle(@NonNull GetEngagementAnalyticsQuery query) {
        return this.campaignEngagementRepository.getEngagementAnalytics(
                query.campaignIDS().stream().map(CampaignID::id).collect(Collectors.toSet()));
    }
}
