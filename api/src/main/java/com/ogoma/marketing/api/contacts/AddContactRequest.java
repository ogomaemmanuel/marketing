package com.ogoma.marketing.api.contacts;

import com.ogoma.marketing.core.application.contacts.commands.AddContactCommand;
import com.ogoma.marketing.core.domain.audience.AudienceId;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record AddContactRequest(
        String firstName,
        String lastName,
        String email,
        Map<String, String> attributes,
        Set<UUID> audienceIds

) {
    public AddContactRequest {
        attributes = attributes == null
                ? Map.of()
                : Map.copyOf(attributes);

        audienceIds = audienceIds == null
                ? Set.of()
                : Set.copyOf(audienceIds);
    }
    public AddContactCommand toCommand(String userId) {
        Set<AudienceId> targetAudienceIDs = audienceIds.stream().map(AudienceId::new).collect(Collectors.toUnmodifiableSet());

        return new AddContactCommand(firstName, lastName, email, attributes, targetAudienceIDs, userId);
    }
}
