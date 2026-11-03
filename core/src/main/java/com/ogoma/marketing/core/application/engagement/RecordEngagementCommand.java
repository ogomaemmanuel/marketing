package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.engagment.EngagementType;

import java.util.UUID;

public record RecordEngagementCommand(
        UUID trackingId,
        EngagementType engagementType,
        //Used to detect email clients
        String userAgent,
        String ipAddress
) implements Command<Void> {
}
