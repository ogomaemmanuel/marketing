package com.ogoma.marketing.infrastructure.contacts;

import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public record ContactRepositoryJDBCAdapter(JdbcAggregateTemplate jdbcAggregateTemplate
) implements ContactRepository {

    @Override
    public ContactEntity save(ContactEntity contactEntity) {
        return jdbcAggregateTemplate.save(contactEntity);
    }

    @Override
    public Optional<ContactEntity> findById(ContactID contactID) {
        return Optional.ofNullable(this.jdbcAggregateTemplate.findById(contactID, ContactEntity.class));
    }

    @Override
    public Page<ContactEntity> findAllBy(String searchTerm, Pageable pageable) {
        return null;
    }
}
