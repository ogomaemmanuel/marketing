package com.ogoma.marketing.api.sms.requests;

import com.ogoma.marketing.core.application.sms.commands.UpdateSmsTemplateCommand;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

public record UpdateSmsTemplateRequest(
        String name,
        String description,
        String content
) {
    public UpdateSmsTemplateCommand toCommand(SmsTemplateID smsTemplateID, String userId) {
        return new UpdateSmsTemplateCommand(
                smsTemplateID,
                name,
                description,
                content, userId);
    }
}
