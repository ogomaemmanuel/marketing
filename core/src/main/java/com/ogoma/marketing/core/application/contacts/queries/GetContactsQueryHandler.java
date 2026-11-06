package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import com.ogoma.marketing.core.sharedkernel.CustomAssert;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public record GetContactsQueryHandler(ContactRepository contactRepository) implements QueryHandler<GetContactsQuery, Page<GetContactsView>> {
    @Override
    public @NonNull Class<GetContactsQuery> supports() {
        return GetContactsQuery.class;
    }

    @Override
    public Page<GetContactsView> handle(@NonNull GetContactsQuery query) {
        CustomAssert.notNull(query,()->new IllegalArgumentException("Query must not be null"));
        return this.contactRepository.findAllBy(query.searchTerm(), query.pageable()).map(GetContactsView::new);
    }
}
