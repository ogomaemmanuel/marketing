package com.ogoma.marketing.api.sms;

import com.ogoma.marketing.core.application.sms.CreateSmsTemplateCommand;

public record CreateSmsTemplateRequest(
        String name,
        String content,
        String description
) {

    public CreateSmsTemplateCommand toCommandWith(String userId){
        return new CreateSmsTemplateCommand(name,description,content,userId);
    }

}
