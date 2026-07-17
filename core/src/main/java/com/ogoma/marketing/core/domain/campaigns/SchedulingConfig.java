package com.ogoma.marketing.core.domain.campaigns;

public sealed  interface SchedulingConfig permits SendLaterSchedulingConfig,SendNowSchedulingConfig {
}
