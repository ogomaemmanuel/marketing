package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;

public class CreateEmailTemplateCommandHandler implements CommandHandler<CreateEmailTemplateCommand, Void> {
    private final EmailTemplateRepository emailTemplateRepository;

    public CreateEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public Class<CreateEmailTemplateCommand> supports() {
        return CreateEmailTemplateCommand.class;
    }

    @Override
    public Void handle(CreateEmailTemplateCommand command) {
        var entity = EmailTemplateEntity.createNew(
                command.name(),
                command.createdBy(),
                command.emailTemplate());
        this.emailTemplateRepository.saveTemplate(entity);
        return null;
    }
}
