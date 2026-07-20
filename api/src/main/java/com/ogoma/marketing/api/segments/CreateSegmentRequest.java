package com.ogoma.marketing.api.segments;

import com.ogoma.marketing.core.application.segments.CreateSegmentCommand;
import com.ogoma.marketing.core.domain.audience.RuleSet;

public record CreateSegmentRequest(
        String name,
        String description,
        RuleSet ruleSet
) {
    public CreateSegmentCommand toCommand(String userId) {
        return new CreateSegmentCommand(name, description, ruleSet, userId);
    }
}
