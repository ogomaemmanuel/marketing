package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.NotificationWorkflowStarterService;
import org.jspecify.annotations.NonNull;

public record SendTransactionalEmailCommandHandler(
        NotificationWorkflowStarterService notificationWorkflowStarterService
) implements CommandHandler<SendTransactionalEmailCommand, Void> {


    @Override
    public @NonNull Class<SendTransactionalEmailCommand> supports() {
        return SendTransactionalEmailCommand.class;
    }

    @Override
    public Void handle(@NonNull SendTransactionalEmailCommand command) {
        notificationWorkflowStarterService.startWorkflow(command);
        return null;
    }
}
