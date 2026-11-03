package com.ogoma.marketing.core.application.engagement;

import com.ogoma.marketing.core.domain.engagment.EngagementType;

import java.util.UUID;

public record RecordClickEngagementCommand(
        UUID trackingId,
        EngagementType engagementType,
        //Used to detect email clients
        String userAgent,
        String destinationUrl,
        String ipAddress
) {
}
