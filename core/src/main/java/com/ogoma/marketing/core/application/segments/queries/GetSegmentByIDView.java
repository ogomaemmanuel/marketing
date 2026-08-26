package com.ogoma.marketing.core.application.segments.queries;

import com.ogoma.marketing.core.domain.audience.RuleSet;

import java.time.Instant;

public record GetSegmentByIDView(
        String name,

        String description,
        Instant createdAt,
        RuleSet ruleSet
) {
}
