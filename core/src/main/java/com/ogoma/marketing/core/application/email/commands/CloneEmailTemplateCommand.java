package com.ogoma.marketing.core.application.email.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;

public record CloneEmailTemplateCommand(
        EmailTemplateID id,
        String suggestedName,
        String user
) implements Command<Void> {

}
