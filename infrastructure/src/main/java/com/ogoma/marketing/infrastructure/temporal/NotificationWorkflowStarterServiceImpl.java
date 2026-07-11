package com.ogoma.marketing.infrastructure.temporal;

import com.ogoma.marketing.core.abstractions.NotificationWorkflowStarterService;
import com.ogoma.marketing.core.application.transactionalmessages.TransactionalNotification;
import com.ogoma.marketing.infrastructure.workflows.abstractions.MessageSenderWorkflow;
import com.ogoma.marketing.infrastructure.configs.TemporalProperties;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public record NotificationWorkflowStarterServiceImpl(
        WorkflowClient workflowClient,
        TemporalProperties temporalProperties) implements NotificationWorkflowStarterService {

    @Override
    public void startWorkflow(TransactionalNotification transactionalNotification) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(temporalProperties.messageSenderQueue())
                .setWorkflowId(UUID.randomUUID().toString())
                .build();
        MessageSenderWorkflow messageSenderWorkflow =
                workflowClient.newWorkflowStub(MessageSenderWorkflow.class, options);
        WorkflowClient.start(messageSenderWorkflow::startWorkflow, transactionalNotification);
    }
}
