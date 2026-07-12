package com.ogoma.marketing.core.application.audience.queries;

import com.ogoma.marketing.core.domain.audience.AudienceEntity;

import java.util.UUID;

public record GetAudienceByIDView(UUID id, String name) {

    public GetAudienceByIDView(
            AudienceEntity audienceEntity
    ) {
        this(audienceEntity.getId().id(), audienceEntity.getName());
    }
}
