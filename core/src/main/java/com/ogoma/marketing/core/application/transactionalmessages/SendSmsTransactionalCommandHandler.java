package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.NotificationWorkflowStarterService;
import org.jspecify.annotations.NonNull;

public class SendSmsTransactionalCommandHandler implements CommandHandler<SendSmsTransactionalCommand, Void> {

    private final NotificationWorkflowStarterService notificationWorkflowStarterService;

    public SendSmsTransactionalCommandHandler(NotificationWorkflowStarterService notificationWorkflowStarterService) {
        this.notificationWorkflowStarterService = notificationWorkflowStarterService;
    }

    @Override
    public @NonNull Class<SendSmsTransactionalCommand> supports() {
        return SendSmsTransactionalCommand.class;
    }

    @Override
    public Void handle(@NonNull SendSmsTransactionalCommand command) {
        notificationWorkflowStarterService.startWorkflow(command);
        return null;

    }
}
