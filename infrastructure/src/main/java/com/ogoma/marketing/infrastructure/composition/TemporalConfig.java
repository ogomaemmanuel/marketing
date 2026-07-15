package com.ogoma.marketing.infrastructure.composition;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(TemporalProperties.class)
public class TemporalConfig {
    @Bean
    public WorkflowClient workflowClient(TemporalProperties appProperties) {
        var service = WorkflowServiceStubs
                .newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(appProperties.address()).build());
        var clientOptions = WorkflowClientOptions.newBuilder()
                .setNamespace(appProperties.namespace())
                .build();
        return WorkflowClient.newInstance(service, clientOptions);
    }
}
