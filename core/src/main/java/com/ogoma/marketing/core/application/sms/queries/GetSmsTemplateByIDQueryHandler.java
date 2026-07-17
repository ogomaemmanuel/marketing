package com.ogoma.marketing.core.application.sms.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;

public record GetSmsTemplateByIDQueryHandler(
        SmsTemplateRepository smsTemplateRepository
) implements QueryHandler<GetSmsTemplateByIDQuery, GetSmsTemplateByIDView> {
    @Override
    public Class<GetSmsTemplateByIDQuery> supports() {
        return GetSmsTemplateByIDQuery.class;
    }

    @Override
    public GetSmsTemplateByIDView handle(GetSmsTemplateByIDQuery query) {
        return this.smsTemplateRepository.findSmsTemplateByID(query.smsTemplateID()).map(GetSmsTemplateByIDView::new)
                .orElseThrow(() -> new RecordNotFoundException("Sms template not found"));
    }
}
