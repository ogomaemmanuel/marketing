package com.ogoma.marketing.api.email;

import com.ogoma.marketing.core.application.email.commands.UpdateEmailTemplateCommand;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntityID;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;

public record UpdateEmailTemplateRequest(
        String name,
        EmailTemplate emailTemplate) {

    public UpdateEmailTemplateCommand toCommandWith(EmailTemplateEntityID templateEntityID, String userId) {
        return new UpdateEmailTemplateCommand(templateEntityID, name, userId, emailTemplate);
    }
}
