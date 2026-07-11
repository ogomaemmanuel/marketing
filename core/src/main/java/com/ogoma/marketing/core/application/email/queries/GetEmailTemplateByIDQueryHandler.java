package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;

import java.util.Optional;

public record GetEmailTemplateByIDQueryHandler(
        EmailTemplateRepository emailTemplateRepository
) implements QueryHandler<GetEmailTemplateByIDQuery, Optional<GetEmailTemplateByIDView>> {
    @Override
    public Class<GetEmailTemplateByIDQuery> supports() {
        return GetEmailTemplateByIDQuery.class;
    }

    @Override
    public Optional<GetEmailTemplateByIDView> handle(GetEmailTemplateByIDQuery query) {
        return this.emailTemplateRepository.getTemplateByID(query.id()).map(GetEmailTemplateByIDView::new);
    }
}
