package com.ogoma.marketing.core.abstractions;

import com.ogoma.marketing.core.application.transactionalmessages.TransactionalNotification;

public interface NotificationWorkflowStarterService {
    void startWorkflow(TransactionalNotification transactionalNotification);
}
