package com.ogoma.marketing.core.domain.engagment;

import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Table(name = "campaign_engagements")
@Getter
public class CampaignEngagement extends AggregateRoot<CampaignEngagementID> {
    private UUID trackingId;
    private EngagementType engagementType;
    private Instant receivedAt;
    private String userAgent;
    private String destinationUrl;
    private String ipAddress;


    private CampaignEngagement(
            UUID trackingId,
            EngagementType engagementType, Instant receivedAt) {
        super(new CampaignEngagementID());
        this.trackingId = trackingId;
        this.engagementType = engagementType;
        this.receivedAt = receivedAt;
    }

    public static CampaignEngagement createNew(
            UUID trackingId,
            EngagementType engagementType,
            Clock clock) {
        return new CampaignEngagement(trackingId, engagementType, clock.instant());
    }
}
