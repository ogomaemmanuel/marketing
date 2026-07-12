package com.ogoma.marketing.core.application.audience.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceId;

public record UpdateAudienceCommand(
        AudienceId audienceId,
        String name,
        String userId

        ) implements Command<Void> {
}
