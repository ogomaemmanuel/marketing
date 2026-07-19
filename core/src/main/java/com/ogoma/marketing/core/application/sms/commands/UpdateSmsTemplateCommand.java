package com.ogoma.marketing.core.application.sms.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

public record UpdateSmsTemplateCommand(
        SmsTemplateID templateID,
        String name,
        String description,
        String content,
        String userId
) implements Command<Void> {
}
