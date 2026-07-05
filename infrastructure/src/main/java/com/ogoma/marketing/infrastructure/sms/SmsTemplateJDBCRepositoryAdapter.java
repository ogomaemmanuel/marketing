package com.ogoma.marketing.infrastructure.sms;

import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
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
public class SmsTemplateJDBCRepositoryAdapter implements SmsTemplateRepository {
    private final SmsTemplateJDBCRepository jdbcRepository;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    public SmsTemplateJDBCRepositoryAdapter(SmsTemplateJDBCRepository jdbcRepository, JdbcAggregateTemplate jdbcAggregateTemplate) {
        this.jdbcRepository = jdbcRepository;
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public SmsTemplateEntity saveSmsTemplate(SmsTemplateEntity smsTemplateEntity) {
        return jdbcRepository.save(smsTemplateEntity);
    }

    @Override
    public Optional<SmsTemplateEntity> findSmsTemplateByID(SmsTemplateID smsTemplateID) {
        return this.jdbcRepository.findById(smsTemplateID);
    }

    @Override
    public Page<SmsTemplateEntity> findSmsTemplates(Pageable pageable) {
        return jdbcRepository.findAll(pageable);
    }

    @Override
    public Page<SmsTemplateEntity> findSmsTemplates(String searchTerm, Pageable pageable) {
        if (!StringUtils.hasText(searchTerm)) {
            return this.findSmsTemplates(pageable);
        }
        var criteria = Criteria.from(Criteria.where(PropertyPath.of(SmsTemplateEntity::getName))
                .like("%" + searchTerm.trim() + "%")
                .ignoreCase(true).or(Criteria.where(PropertyPath.of(SmsTemplateEntity::getDescription))
                        .like("%" + searchTerm.trim() + "%")
                        .ignoreCase(true)
                )
        );
        Query countQuery = Query.query(criteria);
        long count = jdbcAggregateTemplate.count(countQuery, SmsTemplateEntity.class);
        if(count==0){
            return Page.empty(pageable);
        }
        Query listQuery = Query.query(criteria).with(pageable);
        List<SmsTemplateEntity> smsTemplateEntities =
                jdbcAggregateTemplate.findAll(listQuery, SmsTemplateEntity.class);
        return new PageImpl<>(smsTemplateEntities, pageable, count);
    }
}
