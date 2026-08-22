package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import org.springframework.data.domain.Page;

public record GetEmailTemplatesQueryHandler(EmailTemplateRepository emailTemplateRepository
) implements QueryHandler<GetEmailTemplatesQuery, Page<GetEmailTemplatesListItemView>> {
    @Override
    public Class<GetEmailTemplatesQuery> supports() {
        return GetEmailTemplatesQuery.class;
    }

    @Override
    public Page<GetEmailTemplatesListItemView> handle(GetEmailTemplatesQuery query) {
        return this.emailTemplateRepository.getEmailTemplates(query.pageable()).map(emailTemplateEntity -> new GetEmailTemplatesListItemView(
                emailTemplateEntity.getId().id(),
                emailTemplateEntity.getName(),
                emailTemplateEntity.getCreatedAt(),
                emailTemplateEntity.getUpdatedAt()
        ));
    }
}
