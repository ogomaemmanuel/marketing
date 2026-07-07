package com.ogoma.marketing.core.domain.users;

import org.springframework.util.Assert;

import java.util.UUID;

public record UserID(UUID id) {
    public UserID {
        Assert.notNull(id, "%s id is required".formatted(UserID.class.getSimpleName()));
    }

    public UserID() {
        this(UUID.randomUUID());
    }
}
