package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import org.jspecify.annotations.NonNull;

public record GetContactByIDQueryHandler(
        ContactRepository contactRepository) implements QueryHandler<GetContactByIDQuery, GetContactByIDView> {
    @Override
    public @NonNull Class<GetContactByIDQuery> supports() {
        return GetContactByIDQuery.class;
    }

    @Override
    public GetContactByIDView handle(@NonNull GetContactByIDQuery query) {
        return contactRepository.findDetailsById(query.contactID()).orElseThrow(() -> new RecordNotFoundException("Contact not found"));
    }
}
