package com.ogoma.marketing.infrastructure.workflows.abstractions;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import io.temporal.activity.ActivityInterface;

import java.util.Optional;
@ActivityInterface
public interface GetEmailTemplateActivity extends WorkflowActivity {
    Optional<EmailTemplateEntity> getEmailTemplate(EmailTemplateID id);
}
