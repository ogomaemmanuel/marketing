package com.ogoma.marketing.infrastructure.workflows.abstractions;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SendCampaignWorkflow {
    @WorkflowMethod
    void start(StartSendCampaignWorkflowCommand startRequest);
}
