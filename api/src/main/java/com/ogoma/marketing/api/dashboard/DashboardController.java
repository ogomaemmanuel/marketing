package com.ogoma.marketing.api.dashboard;

import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.dashboard.queries.GetStatsQuery;
import com.ogoma.marketing.core.application.dashboard.queries.GetStatsQueryView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public record DashboardController(QueryDispatcher queryDispatcher) {

    @GetMapping("/stats")
    public GetStatsQueryView getStas() {
        return this.queryDispatcher.dispatch(new GetStatsQuery());
    }
}
