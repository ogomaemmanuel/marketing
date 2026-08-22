package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record GetEmailTemplatesQuery(Pageable pageable,
                                     String searchTerm) implements Query<Page<GetEmailTemplatesListItemView>> {

}
