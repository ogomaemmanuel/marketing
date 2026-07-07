package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;

public record UpdateEmailTemplateCommand(
        EmailTemplateID id,
        String name,
        String userID,
        EmailTemplate emailTemplate
) implements Command<Void> {

}
