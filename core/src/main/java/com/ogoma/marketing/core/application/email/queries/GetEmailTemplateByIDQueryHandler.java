package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;

import java.util.Optional;

public class GetEmailTemplateByIDQueryHandler implements QueryHandler<GetEmailTemplateByIDQuery, Optional<GetEmailTemplateByIDView>> {
    private final EmailTemplateRepository emailTemplateRepository;

    public GetEmailTemplateByIDQueryHandler(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public Class<GetEmailTemplateByIDQuery> supports() {
        return GetEmailTemplateByIDQuery.class;
    }

    @Override
    public Optional<GetEmailTemplateByIDView> handle(GetEmailTemplateByIDQuery query) {
        return this.emailTemplateRepository.getTemplateByID(query.id()).map(GetEmailTemplateByIDView::new);
    }
}
