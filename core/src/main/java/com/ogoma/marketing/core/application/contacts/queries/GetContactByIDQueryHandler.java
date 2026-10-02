package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

public record GetContactByIDQueryHandler(
        ContactRepository contactRepository) implements QueryHandler<GetContactByIDQuery, GetContactByIDView> {
    @Override
    public Class<GetContactByIDQuery> supports() {
        return GetContactByIDQuery.class;
    }

    @Override
    public GetContactByIDView handle(GetContactByIDQuery query) {
        return contactRepository.findDetailsById(query.contactID()).orElseThrow(() -> new RecordNotFoundException("Contact not found"));
    }
}
