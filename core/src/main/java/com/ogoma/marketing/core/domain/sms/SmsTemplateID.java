package com.ogoma.marketing.core.domain.sms;

import org.springframework.util.Assert;

import java.util.UUID;

public record SmsTemplateID(UUID id) {

    public SmsTemplateID {
        Assert.notNull(id, String.format("%s id is required", SmsTemplateID.class.getSimpleName()));
    }

    public SmsTemplateID() {
        this(UUID.randomUUID());
    }
}
