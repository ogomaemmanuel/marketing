package com.ogoma.marketing.core.application.sms;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;

public record UpdateSmsTemplateCommandHandler(
        SmsTemplateRepository smsTemplateRepository
) implements CommandHandler<UpdateSmsTemplateCommand, Void> {
    @Override
    public Class<UpdateSmsTemplateCommand> supports() {
        return UpdateSmsTemplateCommand.class;
    }

    @Override
    public Void handle(UpdateSmsTemplateCommand command) {
        SmsTemplateEntity smsTemplateEntity = this.smsTemplateRepository.findSmsTemplateByID(command.templateID()).orElseThrow(() -> new RecordNotFoundException("Sms template not found"));
        smsTemplateEntity.update(
                command.name(),
                command.description(),
                command.content(),
                command.userId()
        );
        this.smsTemplateRepository.saveSmsTemplate(smsTemplateEntity);
        return null;
    }
}
