package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record GetEmailTemplateByIDQueryHandler(
        EmailTemplateRepository emailTemplateRepository
) implements QueryHandler<GetEmailTemplateByIDQuery, Optional<GetEmailTemplateByIDView>> {
    @Override
    public @NonNull Class<GetEmailTemplateByIDQuery> supports() {
        return GetEmailTemplateByIDQuery.class;
    }

    @Override
    public Optional<GetEmailTemplateByIDView> handle(@NonNull GetEmailTemplateByIDQuery query) {
        return this.emailTemplateRepository.getTemplateByID(query.id()).map(GetEmailTemplateByIDView::new);
    }
}
