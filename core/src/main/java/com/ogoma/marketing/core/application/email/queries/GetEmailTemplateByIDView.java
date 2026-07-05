package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;

import java.time.Instant;
import java.util.UUID;

public record GetEmailTemplateByIDView(
        UUID id, String name, EmailTemplate emailTemplate, Instant createdAt
) {
    public GetEmailTemplateByIDView(EmailTemplateEntity entity) {
        this(
                entity.getId().id(),
                entity.getName(),
                entity.getEmailTemplate(),
                entity.getCreatedAt());
    }
}
