package com.ogoma.marketing.core.application.sms.queries;

import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;

import java.time.Instant;

public record GetSmsTemplatesView(
        String name,
        String description,
        String content,
        Instant createdAt
) {

    public GetSmsTemplatesView(SmsTemplateEntity smsTemplateEntity) {
        this(
                smsTemplateEntity.getName(),
                smsTemplateEntity.getDescription(),
                smsTemplateEntity.getContent(),
                smsTemplateEntity.getCreatedAt());
    }
}
