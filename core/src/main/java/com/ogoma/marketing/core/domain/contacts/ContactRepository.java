package com.ogoma.marketing.core.domain.contacts;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContactRepository {
    ContactEntity save(ContactEntity contactEntity);

    Optional<ContactEntity> findById(ContactID contactID);

    ContactEntity findAllBy(String searchTerm, Pageable pageable);
}
