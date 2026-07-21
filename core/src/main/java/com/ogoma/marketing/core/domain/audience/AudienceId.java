package com.ogoma.marketing.core.domain.audience;

import com.ogoma.marketing.core.sharedkernel.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record AudienceId(UUID id) implements TypedID<UUID> {
    public AudienceId {
        Assert.notNull(id, "%s id is required".formatted(this.getClass().getSimpleName()));
    }

    public AudienceId() {
        this(UUID.randomUUID());
    }
}
