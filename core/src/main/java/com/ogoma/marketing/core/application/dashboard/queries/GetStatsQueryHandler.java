package com.ogoma.marketing.core.application.dashboard.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;

public record GetStatsQueryHandler(
        DashboardService dashboardService) implements QueryHandler<GetStatsQuery, GetStatsQueryView> {
    @Override
    public Class<GetStatsQuery> supports() {
        return GetStatsQuery.class;
    }

    @Override
    public GetStatsQueryView handle(GetStatsQuery query) {
        return dashboardService.getStats();
    }
}
