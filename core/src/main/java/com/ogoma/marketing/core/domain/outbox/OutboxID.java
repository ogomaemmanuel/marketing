package com.ogoma.marketing.core.domain.outbox;

import org.springframework.util.Assert;

import java.util.UUID;

public record OutboxID(UUID id) {

    public OutboxID {
        Assert.notNull(id, "%s id is required".formatted(OutboxID.class.getSimpleName()));
    }

    public OutboxID() {
        this(UUID.randomUUID());
    }
}
