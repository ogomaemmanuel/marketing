package com.ogoma.marketing.core.domain.campaigns;

import java.time.ZonedDateTime;

public record SendNowSchedulingConfig(
        ZonedDateTime sendTime
) implements SchedulingConfig {
}
