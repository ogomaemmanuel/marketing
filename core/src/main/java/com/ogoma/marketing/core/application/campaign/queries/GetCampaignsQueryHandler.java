package com.ogoma.marketing.core.application.campaign.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import org.springframework.data.domain.Page;

public record GetCampaignsQueryHandler(
        CampaignRepository campaignRepository
) implements QueryHandler<GetCampaignsQuery, Page<GetCampaignsView>> {
    @Override
    public Class<GetCampaignsQuery> supports() {
        return GetCampaignsQuery.class;
    }

    @Override
    public Page<GetCampaignsView> handle(GetCampaignsQuery query) {
        return campaignRepository.findAllBy(query.searchTerm(), query.pageable()).map(GetCampaignsView::new);
    }
}
