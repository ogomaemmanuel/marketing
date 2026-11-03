package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.application.engagement.GetEngagementAnalyticsQueryHandler;
import com.ogoma.marketing.core.application.engagement.RecordEngagementCommandHandler;
import com.ogoma.marketing.core.domain.engagment.CampaignEngagementRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class CampaignEngagementConfig {


    @Bean
    RecordEngagementCommandHandler recordEngagementCommandHandler(
            Clock clock,
            CampaignEngagementRepository campaignEngagementRepository
    ) {
        return new RecordEngagementCommandHandler(clock, campaignEngagementRepository);
    }
    @Bean
    GetEngagementAnalyticsQueryHandler getEngagementAnalyticsQueryHandler(
            CampaignEngagementRepository campaignEngagementRepository
    ) {
        return new GetEngagementAnalyticsQueryHandler(campaignEngagementRepository);
    }
}
