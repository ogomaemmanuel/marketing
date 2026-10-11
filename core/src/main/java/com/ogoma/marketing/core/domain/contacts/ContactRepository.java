package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.application.contacts.commands.ExportContactsView;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDView;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface ContactRepository {
    ContactEntity save(ContactEntity contactEntity);

    Iterable<ContactEntity> saveAll(Collection<ContactEntity> contactEntity);

    Optional<ContactEntity> findById(ContactID contactID);

    Stream<ExportContactsView> findAllByAudienceIds(Set<AudienceId> audienceIds);

    Optional<GetContactByIDView> findDetailsById(ContactID contactID);

    Page<ContactEntity> findAllBy(String searchTerm, Pageable pageable);
}
