package com.ogoma.marketing.core.domain.campaigns;

import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import org.springframework.data.relational.core.mapping.Table;


@Table("campaign_audience")
public record CampaignAudience(
        AudienceId audienceId) {
    public CampaignAudience {
        CustomAssert.notNull(audienceId, () -> new IllegalArgumentException("Audience id is required"));
    }
}
