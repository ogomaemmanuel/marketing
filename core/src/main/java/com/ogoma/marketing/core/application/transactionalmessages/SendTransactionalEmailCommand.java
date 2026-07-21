package com.ogoma.marketing.core.application.transactionalmessages;


import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public record SendTransactionalEmailCommand(
        EmailTemplateID templateID,
        List<String> recipients,
        Map<String, Object> params,
        ZonedDateTime scheduledAt
) implements Command<Void>, TransactionalNotification {
}
