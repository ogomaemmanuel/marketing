package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;

public class UpdateEmailTemplateCommandHandler implements CommandHandler<UpdateEmailTemplateCommand, Void> {
    private final EmailTemplateRepository emailTemplateRepository;

    public UpdateEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public Class<UpdateEmailTemplateCommand> supports() {
        return UpdateEmailTemplateCommand.class;
    }

    @Override
    public Void handle(UpdateEmailTemplateCommand command) {
        this.emailTemplateRepository
                .getTemplateByID(command.id()).map(entity -> {
                    entity.updateDetails(command.name(), command.userID(), command.emailTemplate());
                    return this.emailTemplateRepository.saveTemplate(entity);
                });
        return null;
    }
}
