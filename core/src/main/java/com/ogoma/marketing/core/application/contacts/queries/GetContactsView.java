package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.domain.contacts.ContactAttributeValue;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record GetContactsView(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Map<String, String> attributes

) {
    public GetContactsView {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }

    public GetContactsView(ContactEntity entity) {
        this(entity.getId().id(), entity.getEmail(), entity.getFirstName(), entity.getLastName(), entity.getAttributes().stream().collect(Collectors.toMap(ContactAttributeValue::attribute,ContactAttributeValue::value)));
    }
}
