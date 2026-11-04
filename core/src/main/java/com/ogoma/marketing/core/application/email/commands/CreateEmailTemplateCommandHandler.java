package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;

public record CreateEmailTemplateCommandHandler(
        EmailTemplateRepository emailTemplateRepository
) implements CommandHandler<CreateEmailTemplateCommand, EmailTemplateID> {
    @Override
    public Class<CreateEmailTemplateCommand> supports() {
        return CreateEmailTemplateCommand.class;
    }

    @Override
    public EmailTemplateID handle(CreateEmailTemplateCommand command) {
        var entity = EmailTemplateEntity.createNew(
                command.name(),
                command.createdBy(),
                command.emailTemplate());
        this.emailTemplateRepository.saveTemplate(entity);
        return entity.getId();
    }
}
