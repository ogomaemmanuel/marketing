package com.ogoma.marketing.core.application.campaign.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import org.jspecify.annotations.NonNull;

public record GetCampaignByIDQueryHandler(
        CampaignRepository campaignRepository
) implements QueryHandler<GetCampaignByIDQuery, GetCampaignByIDView> {
    @Override
    public @NonNull Class<GetCampaignByIDQuery> supports() {
        return GetCampaignByIDQuery.class;
    }

    @Override
    public GetCampaignByIDView handle(@NonNull GetCampaignByIDQuery query) {
        return campaignRepository.findDetailsByID(query.campaignID()).orElseThrow(()->new RecordNotFoundException("Campaign not found"));
    }
}
