package com.ogoma.marketing.core.domain.segments;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record SegmentID(UUID id) implements TypedID<UUID> {
    public SegmentID {
        Assert.notNull(id, "%s id is required".formatted(SegmentID.class.getSimpleName()));
    }

    public SegmentID() {
        this(UUID.randomUUID());
    }
}
