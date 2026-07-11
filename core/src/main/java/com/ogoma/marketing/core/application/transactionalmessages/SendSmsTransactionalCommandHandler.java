package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.NotificationWorkflowStarterService;

public class SendSmsTransactionalCommandHandler implements CommandHandler<SendSmsTransactionalCommand, Void> {

    private final NotificationWorkflowStarterService notificationWorkflowStarterService;

    public SendSmsTransactionalCommandHandler(NotificationWorkflowStarterService notificationWorkflowStarterService) {
        this.notificationWorkflowStarterService = notificationWorkflowStarterService;
    }

    @Override
    public Class<SendSmsTransactionalCommand> supports() {
        return SendSmsTransactionalCommand.class;
    }

    @Override
    public Void handle(SendSmsTransactionalCommand command) {
        notificationWorkflowStarterService.startWorkflow(command);
        return null;

    }
}
