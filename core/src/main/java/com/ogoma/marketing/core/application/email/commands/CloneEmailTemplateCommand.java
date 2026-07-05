package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntityID;

public record CloneEmailTemplateCommand(
        EmailTemplateEntityID id,
        String suggestedName,
        String user
) implements Command<Void> {

}
