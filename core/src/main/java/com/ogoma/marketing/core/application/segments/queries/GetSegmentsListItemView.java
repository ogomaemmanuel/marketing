package com.ogoma.marketing.core.application.segments.queries;

import java.time.Instant;
import java.util.UUID;

public record GetSegmentsListItemView(
        UUID id,
        String name,
        String description,
        Instant createdAt
) {

}
