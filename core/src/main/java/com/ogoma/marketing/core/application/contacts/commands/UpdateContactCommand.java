package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactID;

import java.util.Map;
import java.util.Set;

public record UpdateContactCommand(
        ContactID contactID,
        String firstName,
        String lastName,
        String email,
        Map<String, String> attributes,
        Set<AudienceId> audienceIds,
        String userId
)implements Command<ContactEntity> {
}
