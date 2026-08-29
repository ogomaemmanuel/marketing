package com.ogoma.marketing.core.domain.campaigns;

import com.ogoma.marketing.core.sharedkernel.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CampaignCreatedEvent(UUID aggregateID,
                                   String name,
                                   String description,
                                   Instant createdAt,
                                   String createdBy) implements DomainEvent {


    @Override
    public String aggregateType() {
        return CampaignEntity.class.getName();
    }

    @Override
    public String eventVersion() {
        return "V0";
    }

    @Override
    public String eventName() {
        return "CampaignCreated";
    }

}
