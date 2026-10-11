package com.ogoma.marketing.api.contacts;

import com.ogoma.marketing.core.application.contacts.commands.ExportContactsCommand;
import com.ogoma.marketing.core.domain.audience.AudienceId;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ExportContactsRequest(
        Set<UUID> audienceIds
) {
    public ExportContactsRequest {
        audienceIds = Objects.requireNonNullElse(audienceIds, Set.of());
    }

    ExportContactsCommand toCommand(String userId) {
        return new ExportContactsCommand(audienceIds.stream().map(AudienceId::new).collect(Collectors.toSet()), userId);
    }
}
