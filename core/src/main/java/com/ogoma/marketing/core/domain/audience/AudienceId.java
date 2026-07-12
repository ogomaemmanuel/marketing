package com.ogoma.marketing.core.domain.audience;

import org.springframework.util.Assert;

import java.util.UUID;

public record AudienceId(UUID id) {
    public AudienceId {
        Assert.notNull(id, "%s id is required".formatted(this.getClass().getSimpleName()));
    }

    public AudienceId() {
        this(UUID.randomUUID());
    }
}
