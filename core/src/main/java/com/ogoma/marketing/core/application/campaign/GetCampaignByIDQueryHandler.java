package com.ogoma.marketing.core.application.campaign;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

public record GetCampaignByIDQueryHandler(
        CampaignRepository campaignRepository
) implements QueryHandler<GetCampaignByIDQuery, GetCampaignByIDView> {
    @Override
    public Class<GetCampaignByIDQuery> supports() {
        return GetCampaignByIDQuery.class;
    }

    @Override
    public GetCampaignByIDView handle(GetCampaignByIDQuery query) {
        return campaignRepository.findByID(query.campaignID()).map(GetCampaignByIDView::new).orElseThrow(()->new RecordNotFoundException("Campaign not found"));
    }
}
