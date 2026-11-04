package com.ogoma.marketing.core.application.sms.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import com.ogoma.marketing.core.sharedkernel.exceptions.DuplicateRecordException;

public class CreateSmsTemplateCommandHandler implements CommandHandler<CreateSmsTemplateCommand, SmsTemplateEntity> {
    private final SmsTemplateRepository smsTemplateRepository;

    public CreateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        this.smsTemplateRepository = smsTemplateRepository;
    }

    @Override
    public Class<CreateSmsTemplateCommand> supports() {
        return CreateSmsTemplateCommand.class;
    }

    @Override
    public SmsTemplateEntity handle(CreateSmsTemplateCommand command) {
        if (this.smsTemplateRepository.existByName(command.name())) {
            throw new DuplicateRecordException("Sms template with %s name already exist".formatted(command.name()));
        }
        SmsTemplateEntity smsTemplateEntity = SmsTemplateEntity
                .createNew(command.name(), command.description(), command.content(), command.userId());
        return this.smsTemplateRepository.saveSmsTemplate(smsTemplateEntity);

    }
}
