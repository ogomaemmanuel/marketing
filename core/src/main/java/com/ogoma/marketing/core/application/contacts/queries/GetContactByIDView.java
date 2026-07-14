package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.domain.contacts.ContactEntity;

import java.util.Map;
import java.util.UUID;

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
                contactEntity.getAttributes()
        );
    }
}
