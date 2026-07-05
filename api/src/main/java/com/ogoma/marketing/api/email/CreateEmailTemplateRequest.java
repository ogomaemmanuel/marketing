package com.ogoma.marketing.api.email;

import com.ogoma.marketing.core.application.email.commands.CreateEmailTemplateCommand;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import jakarta.validation.constraints.NotNull;

public record CreateEmailTemplateRequest (
        String name,
        @NotNull
        EmailTemplate emailTemplate
){
   public CreateEmailTemplateCommand toCommandWith(String creator){
        return new CreateEmailTemplateCommand(name,creator,emailTemplate);
    }
}
