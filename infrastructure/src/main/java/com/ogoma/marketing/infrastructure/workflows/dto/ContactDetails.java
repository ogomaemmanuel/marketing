package com.ogoma.marketing.infrastructure.workflows.dto;

import java.util.Map;
import java.util.UUID;

public record ContactDetails(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,

        Map<String, String> attributes) {
}
