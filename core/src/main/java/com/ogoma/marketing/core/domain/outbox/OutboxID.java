package com.ogoma.marketing.core.domain.outbox;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record OutboxID(UUID id) implements TypedID<UUID> {

    public OutboxID {
        Assert.notNull(id, "%s id is required".formatted(OutboxID.class.getSimpleName()));
    }

    public OutboxID() {
        this(UUID.randomUUID());
    }
}
