package com.ogoma.marketing.core.application.sms.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public class GetSmsTemplatesQueryHandler implements QueryHandler<GetSmsTemplatesQuery, Page<GetSmsTemplatesView>> {
    private final SmsTemplateRepository smsTemplateRepository;

    public GetSmsTemplatesQueryHandler(SmsTemplateRepository smsTemplateRepository) {
        this.smsTemplateRepository = smsTemplateRepository;
    }

    @Override
    public @NonNull Class<GetSmsTemplatesQuery> supports() {
        return GetSmsTemplatesQuery.class;
    }

    @Override
    public Page<GetSmsTemplatesView> handle(@NonNull GetSmsTemplatesQuery query) {
        return this.smsTemplateRepository.findSmsTemplates(query.searchTerm(),query.pageable()).map(GetSmsTemplatesView::new);
    }
}
