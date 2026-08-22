package com.ogoma.marketing.core.application.email.queries;

import java.time.Instant;
import java.util.UUID;

public record GetEmailTemplatesListItemView(
        UUID id, String name, Instant createdAt, Instant updatedAt
) {
}
