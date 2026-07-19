package com.ogoma.marketing.core.application.sms.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;

public record CreateSmsTemplateCommand(
        String name,
        String description,
        String content,
        String userId
) implements Command<SmsTemplateEntity> {
}
