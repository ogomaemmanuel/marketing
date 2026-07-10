package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

public record CloneEmailTemplateCommandHandler(
        EmailTemplateRepository emailTemplateRepository
) implements CommandHandler<CloneEmailTemplateCommand, Void> {
    @Override
    public Class<CloneEmailTemplateCommand> supports() {
        return CloneEmailTemplateCommand.class;
    }

    @Override
    public Void handle(CloneEmailTemplateCommand command) {
        var emailTemplateEntity = this.emailTemplateRepository.getTemplateByID(command.id()).orElseThrow(() -> new RecordNotFoundException("Email Template not found"));
        emailTemplateRepository.saveTemplate(emailTemplateEntity.clone(command.suggestedName(), command.user()));
        return null;
    }
}
