package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;
import org.jspecify.annotations.NonNull;

public record GetCampaignComparisonQueryHandler(
        CampaignEngagementRepository campaignEngagementRepository
) implements QueryHandler<GetCampaignComparisonQuery, GetCampaignComparisonView> {
    @Override
    public @NonNull Class<GetCampaignComparisonQuery> supports() {
        return GetCampaignComparisonQuery.class;
    }

    @Override
    public GetCampaignComparisonView handle(@NonNull GetCampaignComparisonQuery query) {
        return campaignEngagementRepository.getCampaignComparisonData();
    }
}
