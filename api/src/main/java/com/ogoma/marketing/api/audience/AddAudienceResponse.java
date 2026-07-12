package com.ogoma.marketing.api.audience;

import com.ogoma.marketing.core.domain.audience.AudienceId;

import java.util.UUID;

public record AddAudienceResponse(UUID id) {

    public AddAudienceResponse(AudienceId audienceID) {
        this(audienceID.id());
    }
}
