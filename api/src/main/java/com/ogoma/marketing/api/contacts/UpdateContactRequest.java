package com.ogoma.marketing.api.contacts;

import com.ogoma.marketing.core.application.contacts.commands.UpdateContactCommand;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UpdateContactRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Email
        String email,
        Map<String, String> attributes,
        Set<UUID> audienceIds

) {

    public UpdateContactRequest {
        attributes = attributes == null
                ? Map.of()
                : Map.copyOf(attributes);

        audienceIds = audienceIds == null
                ? Set.of()
                : Set.copyOf(audienceIds);
    }

    public UpdateContactCommand toCommand(ContactID contactID, String userId) {
        Set<AudienceId> targetAudienceIDs = audienceIds.stream().map(AudienceId::new).collect(Collectors.toUnmodifiableSet());
        return new UpdateContactCommand(contactID, firstName, lastName, email, attributes, targetAudienceIDs, userId);
    }
}
