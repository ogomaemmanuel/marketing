package com.ogoma.marketing.core.domain.contacts;

import org.springframework.util.Assert;

import java.util.UUID;

public record ContactID(UUID id) {
    public ContactID {
        Assert.notNull(id, "%s id is required".formatted(this.getClass().getSimpleName()));
    }

    public ContactID() {
        this(UUID.randomUUID());
    }

}
