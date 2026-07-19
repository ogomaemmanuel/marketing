package com.ogoma.marketing.core.application.sms.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;

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
        SmsTemplateEntity smsTemplateEntity = SmsTemplateEntity
                .createNew(command.name(), command.description(), command.content(), command.userId());
        return this.smsTemplateRepository.saveSmsTemplate(smsTemplateEntity);

    }
}
