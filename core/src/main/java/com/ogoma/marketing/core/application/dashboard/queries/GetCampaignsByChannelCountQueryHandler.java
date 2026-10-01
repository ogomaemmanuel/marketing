package com.ogoma.marketing.core.application.dashboard.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;

import java.util.List;

public record GetCampaignsByChannelCountQueryHandler(DashboardService dashboardService) implements QueryHandler<GetCampaignsByChannelCountQuery, List<GetCampaignsByChannelCountView>> {
    @Override
    public Class<GetCampaignsByChannelCountQuery> supports() {
        return GetCampaignsByChannelCountQuery.class;
    }

    @Override
    public List<GetCampaignsByChannelCountView> handle(GetCampaignsByChannelCountQuery query) {
        return dashboardService.getCampaignsByChannelCount();
    }
}
