package com.ogoma.marketing.core.application.contacts.queries;

import java.util.Map;
import java.util.UUID;

public record GetContactByIDView(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Map<String,String> attributes
) {
}
