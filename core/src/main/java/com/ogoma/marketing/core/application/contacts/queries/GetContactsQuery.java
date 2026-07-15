package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.abstractions.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record GetContactsQuery(
        String searchTerm,
        Pageable pageable
) implements Query<Page<GetContactsView>> {
}
