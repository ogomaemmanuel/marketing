package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record AddContactCommand(
        String firstName,
        String lastName,
        String email,
        Map<String, String> attributes,
        Set<UUID> audienceIds,
        String userId
) implements Command<ContactEntity> {


}
