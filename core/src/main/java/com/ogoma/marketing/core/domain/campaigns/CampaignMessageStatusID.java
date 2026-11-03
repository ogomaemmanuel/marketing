package com.ogoma.marketing.core.domain.campaigns;

import com.ogoma.marketing.core.sharedkernel.UUIDV7Generator;
import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.util.Assert;

import java.util.UUID;

public record CampaignMessageStatusID(UUID id) implements TypedID<UUID> {
    public CampaignMessageStatusID {
        Assert.notNull(id, "%s id is required".formatted(this.getClass()));
    }

    public CampaignMessageStatusID() {
        this(UUIDV7Generator.generate());
    }
}
