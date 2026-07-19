package com.ogoma.marketing.api.sms.requests;

import com.ogoma.marketing.core.application.sms.commands.CreateSmsTemplateCommand;
import jakarta.validation.constraints.NotBlank;

public record CreateSmsTemplateRequest(
        @NotBlank
        String name,
        @NotBlank
        String content,
        String description
) {
    public CreateSmsTemplateCommand toCommandWith(String userId){
        return new CreateSmsTemplateCommand(name,description,content,userId);
    }

}
