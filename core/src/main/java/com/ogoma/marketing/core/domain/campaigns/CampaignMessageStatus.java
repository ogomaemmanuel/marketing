package com.ogoma.marketing.core.domain.campaigns;

import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Table("campaign_message_status")
@Getter
public class CampaignMessageStatus extends AggregateRoot<CampaignMessageStatusID> {
    public enum Status {
        SENT, IN_PROGRESS, FAILED
    }

    private CampaignID campaignID;
    private ContactID contactID;
    private Channel channel;
    private Status status;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private UUID trackingId;

    private CampaignMessageStatus() {
        super(null);
    }

    private CampaignMessageStatus(
            CampaignID campaignID,
            ContactID contactID,
            Channel channel,
            UUID trackingId,
            Instant createdAt
    ) {
        super(new CampaignMessageStatusID());
        this.campaignID = campaignID;
        this.contactID = contactID;
        this.channel = channel;
        this.createdAt = createdAt;
        this.lastUpdatedAt = createdAt;
        this.trackingId = trackingId;
        this.status = Status.IN_PROGRESS;
    }

    public static CampaignMessageStatus createNew(
            CampaignID campaignID,
            ContactID contactID,
            Channel channel,
            UUID trackingId,
            Clock clock
    ) {
        return new CampaignMessageStatus(
                campaignID,
                contactID,
                channel,
                trackingId,
                clock.instant()
        );
    }

    public void markAsSent(Clock clock) {
        this.status = Status.SENT;
        this.lastUpdatedAt = clock.instant();
    }

    public void markAsFailed(Clock clock) {
        this.status = Status.FAILED;
        this.lastUpdatedAt = clock.instant();
    }
}
