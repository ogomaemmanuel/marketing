package com.ogoma.marketing.infrastructure.contacts;

import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
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
        Criteria criteria = Criteria.empty();
        if (StringUtils.hasText(searchTerm)) {
            criteria = Criteria.where(PropertyPath.of(ContactEntity::getLastName)).like("%" + searchTerm.trim() + "%s").ignoreCase(true).
                    or(Criteria.where(PropertyPath.of(ContactEntity::getFirstName)).like("%" + searchTerm.trim() + "%s").ignoreCase(true)
                            .or(Criteria.where(PropertyPath.of(ContactEntity::getEmail)).like("%" + searchTerm.trim() + "%s"))
                    );
        }
        var countQuery = Query.query(criteria);
        long count = jdbcAggregateTemplate.count(countQuery, ContactEntity.class);
        if (count == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        var dataQuery = Query.query(criteria).with(pageable);
        var data = jdbcAggregateTemplate.findAll(dataQuery, ContactEntity.class);
        return new PageImpl<>(data, pageable, count);
    }
}
