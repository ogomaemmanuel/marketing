package com.ogoma.marketing.campaignsbatchprocessor.config;

import com.ogoma.marketing.core.abstractions.EventHandler;
import com.ogoma.marketing.core.abstractions.EventRouter;
import com.ogoma.marketing.core.application.audience.services.AudienceMatcher;
import com.ogoma.marketing.core.application.campaign.eventhandlers.CampaignSentEventHandler;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;
import com.ogoma.marketing.infrastructure.audience.PostgresAudienceMatcher;
import com.ogoma.marketing.infrastructure.campaign.CampaignRepositoryJdbcAdapter;
import com.ogoma.marketing.infrastructure.composition.JDBCConverterRegistry;
import com.ogoma.marketing.infrastructure.composition.JdbcClientConfig;
import com.ogoma.marketing.infrastructure.composition.TemporalConfig;
import com.ogoma.marketing.infrastructure.composition.TemporalProperties;
import com.ogoma.marketing.infrastructure.messaging.EventRouterImpl;
import io.temporal.client.WorkflowClient;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.Clock;
import java.util.List;

@Configuration
@Import({
        JdbcClientConfig.class,
        JDBCConverterRegistry.class,
        CampaignRepositoryJdbcAdapter.class,
        TemporalConfig.class})
@EnableJdbcRepositories(basePackages = "com.ogoma.marketing")
@EntityScan(basePackages = "com.ogoma.marketing")
public class IntegrationConfig {

    @Bean
    CampaignSentEventHandler campaignSentEventHandler(AudienceMatcher audienceMatcher,
                                                      WorkflowClient workflowClient,
                                                      TemporalProperties temporalProperties,
                                                      CampaignRepository campaignRepository) {
        return new CampaignSentEventHandler(workflowClient,temporalProperties.campaignSendingQueue(),audienceMatcher, campaignRepository);
    }

    @Bean
    AudienceMatcher audienceMatcher(JdbcAggregateTemplate jdbcAggregateTemplate, JdbcClient jdbcClient) {
        return new PostgresAudienceMatcher(jdbcAggregateTemplate, jdbcClient);
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    EventRouter eventRouter(List<? extends EventHandler<? extends DomainEvent>> eventHandlers) {
        return new EventRouterImpl(eventHandlers);
    }
}
