package com.ogoma.marketing.infrastructure.composition;

import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendCampaignActivities;
import com.ogoma.marketing.infrastructure.workflows.implementations.SendCampaignActivitiesImpl;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

@Configuration
public class SendCampaignConfig {

    @Bean
    public SendCampaignActivities sendCampaignActivities(
     JdbcAggregateTemplate jdbcAggregateTemplate,
     MessageRouter messageRouter,
     TemplateRenderer templateRenderer,
     DSLContext dslContext

    ){
        return new SendCampaignActivitiesImpl(
                jdbcAggregateTemplate,
                messageRouter,
                templateRenderer,
                dslContext
        );

    }
}
