package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;

public record CreateEmailTemplateCommandHandler(
        EmailTemplateRepository emailTemplateRepository
) implements CommandHandler<CreateEmailTemplateCommand, EmailTemplateID> {
    @Override
    public Class<CreateEmailTemplateCommand> supports() {
        return CreateEmailTemplateCommand.class;
    }

    @Override
    public EmailTemplateID handle(CreateEmailTemplateCommand command) {
        if (this.emailTemplateRepository.existsByName(command.name())) {
            throw new DuplicateRecordException("Email template with %s name already exist".formatted(command.name()));
        }
        var entity = EmailTemplateEntity.createNew(
                command.name(),
                command.createdBy(),
                command.emailTemplate());
        this.emailTemplateRepository.saveTemplate(entity);
        return entity.getId();
    }
}
