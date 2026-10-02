package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContactRepository {
    ContactEntity save(ContactEntity contactEntity);

    Optional<ContactEntity> findById(ContactID contactID);
    Optional<GetContactByIDView> findDetailsById(ContactID contactID);

   Page<ContactEntity> findAllBy(String searchTerm, Pageable pageable);
}
