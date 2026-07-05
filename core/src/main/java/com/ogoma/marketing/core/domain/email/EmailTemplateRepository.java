package com.ogoma.marketing.core.domain.email;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmailTemplateRepository {
    Page<EmailTemplateEntity> getEmailTemplates(Pageable pageable);

    EmailTemplateEntity saveTemplate(EmailTemplateEntity emailTemplateEntity);

    Optional<EmailTemplateEntity> getTemplateByID(EmailTemplateEntityID id);
}
