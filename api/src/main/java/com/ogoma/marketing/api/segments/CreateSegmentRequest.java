package com.ogoma.marketing.api.segments;

import com.ogoma.marketing.core.application.segments.CreateSegmentCommand;
import com.ogoma.marketing.core.domain.audience.RuleSet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSegmentRequest(
        @NotBlank
        String name,
        String description,
        @NotNull
        @Valid
        RuleSet ruleSet
) {
    public CreateSegmentCommand toCommand(String userId) {
        return new CreateSegmentCommand(name, description, ruleSet, userId);
    }
}
