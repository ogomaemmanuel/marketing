package com.ogoma.marketing.core.application.segments.queries;

import com.ogoma.marketing.core.domain.audience.RuleSet;

import java.time.Instant;
import java.util.UUID;

public record GetSegmentByIDView(
        UUID id,
        String name,

        String description,
        Instant createdAt,
        RuleSet ruleSet
) {
}
