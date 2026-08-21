package com.ogoma.marketing.infrastructure.email;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public record EmailTemplateRepositoryJDBCAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate) implements EmailTemplateRepository {
    @Override
    public Page<EmailTemplateEntity> getEmailTemplates(Pageable pageable) {
        Criteria criteria = Criteria.empty();
        Query countQuery = Query.query(criteria);
        Query dataQuery = Query.query(criteria).with(pageable);
        var count = jdbcAggregateTemplate.count(countQuery, EmailTemplateEntity.class);
        if (count == 0L) {
            return Page.empty();
        }
        var data = jdbcAggregateTemplate.findAll(dataQuery, EmailTemplateEntity.class);
        return new PageImpl<>(data, pageable, count);
    }

    @Override
    public EmailTemplateEntity saveTemplate(EmailTemplateEntity emailTemplateEntity) {
        return this.jdbcAggregateTemplate.save(emailTemplateEntity);
    }

    @Override
    public Optional<EmailTemplateEntity> getTemplateByID(EmailTemplateID id) {
        return Optional.ofNullable(this.jdbcAggregateTemplate.findById(id, EmailTemplateEntity.class));
    }
}
