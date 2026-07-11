package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.infrastructure.workflows.abstractions.GetEmailTemplateActivity;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public record GetEmailTemplateActivityImpl(
        EmailTemplateRepository emailTemplateRepository) implements GetEmailTemplateActivity {
    @Override
    public Optional<EmailTemplateEntity> getEmailTemplate(EmailTemplateID id) {
        return emailTemplateRepository.getTemplateByID(id);
    }
}
