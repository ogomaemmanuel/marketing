package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

public record UpdateEmailTemplateCommandHandler(
        EmailTemplateRepository emailTemplateRepository) implements CommandHandler<UpdateEmailTemplateCommand, Void> {

    @Override
    public Class<UpdateEmailTemplateCommand> supports() {
        return UpdateEmailTemplateCommand.class;
    }

    @Override
    public Void handle(UpdateEmailTemplateCommand command) {
        var entity = this.emailTemplateRepository.getTemplateByID(command.id()).orElseThrow(() -> new RecordNotFoundException("Email template not found"));
        entity.updateDetails(command.name(), command.userID(), command.emailTemplate());
        this.emailTemplateRepository.saveTemplate(entity);
        return null;
    }
}
