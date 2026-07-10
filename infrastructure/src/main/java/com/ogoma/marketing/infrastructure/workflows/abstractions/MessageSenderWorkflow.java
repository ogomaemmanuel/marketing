package com.ogoma.marketing.infrastructure.workflows.abstractions;


import com.ogoma.marketing.core.application.transactionalmessages.TransactionalNotification;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MessageSenderWorkflow {
    @WorkflowMethod
    void startWorkflow(TransactionalNotification transactionalNotification);

}
