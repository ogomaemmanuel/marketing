package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

import java.time.ZonedDateTime;
import java.util.Map;


public record SendSmsTransactionalCommand(

        String recipient,
        SmsTemplateID templateID,


        Map<String, Object> params,
        ZonedDateTime scheduledAt
) implements Command<Void>, TransactionalNotification {

}
