package com.ogoma.marketing.core.application.sms;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

import java.util.Optional;

public record DuplicateSmsTemplateCommand(
        SmsTemplateID id,
        String suggestedName,
        String userId) implements Command<Optional<SmsTemplateEntity>> {

}
