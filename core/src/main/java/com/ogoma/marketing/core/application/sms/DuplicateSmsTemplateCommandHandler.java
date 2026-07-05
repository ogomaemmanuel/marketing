package com.ogoma.marketing.core.application.sms;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;

import java.util.Optional;

public class DuplicateSmsTemplateCommandHandler implements CommandHandler<DuplicateSmsTemplateCommand, Optional<SmsTemplateEntity>> {
    private final SmsTemplateRepository smsTemplateRepository;

    public DuplicateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        this.smsTemplateRepository = smsTemplateRepository;
    }

    @Override
    public Class<DuplicateSmsTemplateCommand> supports() {
        return DuplicateSmsTemplateCommand.class;
    }

    @Override
    public Optional<SmsTemplateEntity> handle(DuplicateSmsTemplateCommand command) {
        return this.smsTemplateRepository.findSmsTemplateByID(command.id()).map(smsTemplateEntity -> {
            var duplicateTemplate = smsTemplateEntity.duplicate(command.suggestedName(), command.userId());
            return this.smsTemplateRepository.saveSmsTemplate(duplicateTemplate);
        });
    }
}
