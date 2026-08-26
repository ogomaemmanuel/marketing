package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.domain.contacts.ContactAttributeValue;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record GetContactByIDView(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Map<String, String> attributes
) {
    public GetContactByIDView(
            ContactEntity contactEntity
    ) {
        this(
                contactEntity.getId().id(),
                contactEntity.getFirstName(),
                contactEntity.getLastName(),
                contactEntity.getEmail(),
                contactEntity.getAttributes().stream().collect(Collectors.toMap(ContactAttributeValue::attribute,ContactAttributeValue::value))
        );
    }
}
