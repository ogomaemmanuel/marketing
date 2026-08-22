package com.ogoma.marketing.transacationmessagesender;

import com.ogoma.marketing.core.abstractions.Message;
import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.MessageSenderService;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.core.implementations.MessageRouterImpl;
import com.ogoma.marketing.infrastructure.composition.TemporalProperties;
import com.ogoma.marketing.infrastructure.templaterendering.PeppleTemplateRenderer;
import com.ogoma.marketing.infrastructure.workflows.abstractions.WorkflowActivity;
import com.ogoma.marketing.infrastructure.workflows.implementations.MessageSenderWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;

@Configuration
@EnableJdbcRepositories(basePackages = "com.ogoma.marketing")
@EntityScan(basePackages = "com.ogoma.marketing")
@ComponentScan(basePackages = "com.ogoma.marketing")
public class WorkerFactoryConfig {
    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }
    @Bean
    public Worker workflowWorker(
            WorkerFactory workerFactory,
            TemporalProperties temporalProperties,
            List<WorkflowActivity> workflowActivities
    ) {
        Worker worker =
                workerFactory.newWorker(
                        temporalProperties.messageSenderQueue()
                );

        worker.registerWorkflowImplementationTypes(
                MessageSenderWorkflowImpl.class

        );

        worker.registerActivitiesImplementations(
                workflowActivities.toArray()
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
