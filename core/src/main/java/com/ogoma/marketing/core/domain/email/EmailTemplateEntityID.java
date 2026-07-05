package com.ogoma.marketing.core.domain.email;

import org.springframework.util.Assert;

import java.util.UUID;

public record EmailTemplateEntityID(UUID id) {
    public EmailTemplateEntityID {
        Assert.notNull(id, String.format("%s value cannot be null", EmailTemplateEntityID.class.getSimpleName()));
    }

    public EmailTemplateEntityID() {
        this(UUID.randomUUID());
    }
}
