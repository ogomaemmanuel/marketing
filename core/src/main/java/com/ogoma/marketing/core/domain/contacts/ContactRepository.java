package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface ContactRepository {
    ContactEntity save(ContactEntity contactEntity);

    Iterable<ContactEntity> saveAll(Collection<ContactEntity> contactEntity);

    Optional<ContactEntity> findById(ContactID contactID);

    Optional<GetContactByIDView> findDetailsById(ContactID contactID);

    Page<ContactEntity> findAllBy(String searchTerm, Pageable pageable);
}
