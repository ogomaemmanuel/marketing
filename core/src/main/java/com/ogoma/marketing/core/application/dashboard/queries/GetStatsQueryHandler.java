package com.ogoma.marketing.core.application.dashboard.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import org.jspecify.annotations.NonNull;

public record GetStatsQueryHandler(
        DashboardService dashboardService) implements QueryHandler<GetStatsQuery, GetStatsQueryView> {
    @Override
    public @NonNull Class<GetStatsQuery> supports() {
        return GetStatsQuery.class;
    }

    @Override
    public GetStatsQueryView handle(@NonNull GetStatsQuery query) {
        return dashboardService.getStats();
    }
}
