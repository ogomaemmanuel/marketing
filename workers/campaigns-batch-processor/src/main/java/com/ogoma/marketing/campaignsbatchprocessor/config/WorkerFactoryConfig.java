package com.ogoma.marketing.campaignsbatchprocessor.config;


import com.ogoma.marketing.core.abstractions.Message;
import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.MessageSenderService;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.core.implementations.MessageRouterImpl;
import com.ogoma.marketing.infrastructure.composition.SendCampaignConfig;
import com.ogoma.marketing.infrastructure.composition.TemporalProperties;
import com.ogoma.marketing.infrastructure.templaterendering.PeppleTemplateRenderer;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendCampaignActivities;
import com.ogoma.marketing.infrastructure.workflows.implementations.SendCampaignWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@Import(SendCampaignConfig.class)
public class WorkerFactoryConfig {
    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    public Worker workflowWorker(
            WorkerFactory workerFactory,
            TemporalProperties temporalProperties,
           SendCampaignActivities sendCampaignActivities
    ) {
        Worker worker =
                workerFactory.newWorker(
                        temporalProperties.campaignSendingQueue()
                );

        worker.registerWorkflowImplementationTypes(
                SendCampaignWorkflowImpl.class

        );

        worker.registerActivitiesImplementations(
                sendCampaignActivities
        );

        return worker;
    }

    @Bean
    public ApplicationRunner workerStarter(
            WorkerFactory workerFactory
    ) {

        return args -> workerFactory.start();
    }

    @Bean
    TemplateRenderer templateRenderer() {
        return new PeppleTemplateRenderer();
    }

    @Bean
    MessageRouter messageRouter(List<MessageSenderService<? extends Message>> messageSenderServices) {
        return new MessageRouterImpl(messageSenderServices);
    }

}
