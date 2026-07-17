package com.ogoma.marketing.core.application.sms.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;

public record GetSmsTemplateByIDQuery(
        SmsTemplateID smsTemplateID
) implements Query<GetSmsTemplateByIDView> {


}
