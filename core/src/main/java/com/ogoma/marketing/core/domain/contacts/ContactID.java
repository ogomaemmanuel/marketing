package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record ContactID(UUID id) implements TypedID<UUID> {
    public ContactID {
        Assert.notNull(id, "%s id is required".formatted(this.getClass().getSimpleName()));
    }

    public ContactID() {
        this(UUID.randomUUID());
    }

}
