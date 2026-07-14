package com.ogoma.marketing.api.contacts;

import com.ogoma.marketing.core.application.contacts.commands.AddContactCommand;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record AddContactRequest(
        String firstName,
        String lastName,
        String email,
        Map<String, String> attributes,
        Set<UUID> audienceIds

) {
    public AddContactCommand toCommand(String userId) {
        return new AddContactCommand(firstName, lastName, email, attributes, audienceIds, userId);
    }
}
