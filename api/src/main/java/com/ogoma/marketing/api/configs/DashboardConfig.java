package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.application.dashboard.queries.DashboardService;
import com.ogoma.marketing.core.application.dashboard.queries.GetCampaignsByChannelCountQueryHandler;
import com.ogoma.marketing.core.application.dashboard.queries.GetStatsQueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DashboardConfig {
    @Bean
    GetStatsQueryHandler getStatsQueryHandler(DashboardService dashboardService) {
        return new GetStatsQueryHandler(dashboardService);
    }

    @Bean
    GetCampaignsByChannelCountQueryHandler getCampaignsByChannelCountQuery(DashboardService dashboardService){
        return new GetCampaignsByChannelCountQueryHandler(dashboardService);
    }

}
