package com.ogoma.marketing.core.domain.email;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record EmailTemplateID(UUID id) implements TypedID<UUID> {
    public EmailTemplateID {
        Assert.notNull(id, String.format("%s value cannot be null", EmailTemplateID.class.getSimpleName()));
    }

    public EmailTemplateID() {
        this(UUID.randomUUID());
    }
}
