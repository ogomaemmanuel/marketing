package com.ogoma.marketing.core.application.sms.queries;

import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;

import java.util.UUID;

public record GetSmsTemplateByIDView(
        UUID id,
        String name,
        String description,
        String content
) {

    public GetSmsTemplateByIDView(SmsTemplateEntity smsTemplateEntity) {
        this(
                smsTemplateEntity.getId().id(),
                smsTemplateEntity.getName(),
                smsTemplateEntity.getDescription(),
                smsTemplateEntity.getContent());
    }
}
