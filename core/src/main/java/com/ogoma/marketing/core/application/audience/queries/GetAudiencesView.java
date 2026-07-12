package com.ogoma.marketing.core.application.audience.queries;

import com.ogoma.marketing.core.domain.audience.AudienceEntity;

import java.time.Instant;
import java.util.UUID;

public record GetAudiencesView(

        UUID id,
        String name,
        Instant createdAt) {

    public GetAudiencesView(AudienceEntity audienceEntity) {
        this(audienceEntity.getId().id(), audienceEntity.getName(), audienceEntity.getCreatedAt());
    }
}
