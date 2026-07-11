package com.ogoma.marketing.infrastructure.workflows.abstractions;

import com.ogoma.marketing.core.abstractions.Message;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface SendMessageActivity extends WorkflowActivity {
    void send(Message message);
}
