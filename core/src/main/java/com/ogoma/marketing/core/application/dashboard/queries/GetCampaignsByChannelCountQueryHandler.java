package com.ogoma.marketing.core.application.dashboard.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;

import java.util.Set;

public record GetCampaignsByChannelCountQueryHandler(DashboardService dashboardService) implements QueryHandler<GetCampaignsByChannelCountQuery, Set<GetCampaignsByChannelCountView>> {
    @Override
    public Class<GetCampaignsByChannelCountQuery> supports() {
        return GetCampaignsByChannelCountQuery.class;
    }

    @Override
    public Set<GetCampaignsByChannelCountView> handle(GetCampaignsByChannelCountQuery query) {
        return dashboardService.getCampaignsByChannelCount();
    }
}
