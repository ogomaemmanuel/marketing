package com.ogoma.marketing.core.domain.sms;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record SmsTemplateID(UUID id) implements TypedID<UUID> {

    public SmsTemplateID {
        Assert.notNull(id, String.format("%s id is required", SmsTemplateID.class.getSimpleName()));
    }

    public SmsTemplateID() {
        this(UUID.randomUUID());
    }
}
