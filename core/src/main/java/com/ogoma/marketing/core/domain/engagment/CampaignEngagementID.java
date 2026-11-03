package com.ogoma.marketing.core.domain.engagment;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;

import java.util.UUID;

public record CampaignEngagementID(UUID id) implements TypedID<UUID> {

    public CampaignEngagementID() {
        this(UUID.randomUUID());
    }
}
