package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;

public class CloneEmailTemplateCommandHandler implements CommandHandler<CloneEmailTemplateCommand, Void> {
    private final EmailTemplateRepository emailTemplateRepository;

    public CloneEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public Class<CloneEmailTemplateCommand> supports() {
        return CloneEmailTemplateCommand.class;
    }

    @Override
    public Void handle(CloneEmailTemplateCommand command) {
        this.emailTemplateRepository.getTemplateByID(command.id()).map(emailTemplateEntity ->
                emailTemplateRepository.saveTemplate(emailTemplateEntity.clone(command.suggestedName(), command.user()))
        );
        return null;
    }
}
