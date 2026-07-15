package com.ogoma.marketing.core.domain.campaigns;

import com.ogoma.marketing.core.sharedkernel.CustomAssert;

import java.util.UUID;

public record CampaignID(UUID id) {

    public CampaignID{
        CustomAssert.notNull(id,()->new IllegalStateException("%s id is required".formatted(this.getClass().getSimpleName())));
    }
    public CampaignID() {
        this(UUID.randomUUID());
    }
}
