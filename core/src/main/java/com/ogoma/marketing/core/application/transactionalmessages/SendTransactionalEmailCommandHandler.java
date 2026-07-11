package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.NotificationWorkflowStarterService;

public record SendTransactionalEmailCommandHandler(
        NotificationWorkflowStarterService notificationWorkflowStarterService
) implements CommandHandler<SendTransactionalEmailCommand, Void> {


    @Override
    public Class<SendTransactionalEmailCommand> supports() {
        return SendTransactionalEmailCommand.class;
    }

    @Override
    public Void handle(SendTransactionalEmailCommand command) {
        notificationWorkflowStarterService.startWorkflow(command);
        return null;
    }
}
