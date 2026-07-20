package com.ogoma.marketing.core.application.segments;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.RuleSet;

public record CreateSegmentCommand(
        String name,
        String description,
        RuleSet ruleSet,
        String userId
) implements Command<AudienceId> {

}
