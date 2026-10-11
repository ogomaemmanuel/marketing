package com.ogoma.marketing.api.contacts;

import com.ogoma.marketing.core.application.contacts.commands.ExportContactsCommand;
import com.ogoma.marketing.core.domain.audience.AudienceId;

import java.util.Objects;
import java.util.Set;

public record ExportContactsRequest(
        Set<AudienceId> audienceIds
) {
    public ExportContactsRequest {
        audienceIds = Objects.requireNonNullElse(audienceIds, Set.of());
    }

    ExportContactsCommand toCommand(String userId) {
        return new ExportContactsCommand(audienceIds,userId);
    }
}
