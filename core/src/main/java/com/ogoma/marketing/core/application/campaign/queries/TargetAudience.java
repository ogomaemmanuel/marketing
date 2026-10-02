package com.ogoma.marketing.core.application.campaign.queries;

import java.util.UUID;

public record TargetAudience(
        UUID id,
        String name
) {
}
