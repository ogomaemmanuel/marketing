package com.ogoma.marketing.infrastructure.contacts;

import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDView;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import com.ogoma.marketing.infrastructure.jooq.tables.ContactAttributeValues;
import com.ogoma.marketing.infrastructure.jooq.tables.Contacts;
import org.jooq.DSLContext;
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
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;


@Component
public record ContactRepositoryJDBCAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate,
        DSLContext dslContext
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
    public Optional<GetContactByIDView> findDetailsById(ContactID contactID) {
        var c = Contacts.CONTACTS;
        var catt = ContactAttributeValues.CONTACT_ATTRIBUTE_VALUES;
        var attributesField = multiset(
                select(
                        catt.ATTRIBUTE,
                        catt.VALUE
                ).from(catt).where(catt.CONTACT_ID.eq(c.ID))
        )
                .convertFrom(result ->
                        result.collect(
                                Collectors.toMap(
                                        r -> r.get(catt.ATTRIBUTE),
                                        r -> r.get(catt.VALUE)
                                )
                        )
                )
                .as("attributes");
        return dslContext.select(
                        c.ID,
                        c.FIRST_NAME,
                        c.LAST_NAME,
                        c.EMAIL,
                        attributesField


                )
                .from(c)
                .where(c.ID.eq(contactID.id()))
                .fetchOptional(r -> new GetContactByIDView(
                        r.get(c.ID),
                        r.get(c.FIRST_NAME),
                        r.get(c.LAST_NAME),
                        r.get(c.EMAIL),
                        r.get(attributesField)
                ));
    }


    @Override
    public Page<ContactEntity> findAllBy(String searchTerm, Pageable pageable) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.hasText(searchTerm)) {
            criteria = Criteria.where(PropertyPath.of(ContactEntity::getLastName)).like("%" + searchTerm.trim() + "%").ignoreCase(true).
                    or(Criteria.where(PropertyPath.of(ContactEntity::getFirstName)).like("%" + searchTerm.trim() + "%").ignoreCase(true)
                            .or(Criteria.where(PropertyPath.of(ContactEntity::getEmail)).like("%" + searchTerm.trim() + "%").ignoreCase(true))
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
