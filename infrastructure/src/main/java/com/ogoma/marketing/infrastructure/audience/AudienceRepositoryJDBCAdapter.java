package com.ogoma.marketing.infrastructure.audience;

import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public record AudienceRepositoryJDBCAdapter(JdbcAggregateTemplate jdbcAggregateTemplate) implements AudienceRepository {
    @Override
    public AudienceEntity save(AudienceEntity audienceEntity) {
        return this.jdbcAggregateTemplate.save(audienceEntity);
    }

    @Override
    public Optional<AudienceEntity> findById(AudienceId audienceID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(audienceID, AudienceEntity.class));
    }

    @Override
    public Page<AudienceEntity> findAllBy(Pageable pageable) {
        var listQuery = Query.query(CriteriaDefinition.empty()).with(pageable);
        var countQuery = Query.query(CriteriaDefinition.empty());
        var count = jdbcAggregateTemplate.count(countQuery, AudienceEntity.class);
        if (count == 0L) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0L);
        }
        var data = jdbcAggregateTemplate.findAll(listQuery, AudienceEntity.class);
        return new PageImpl<>(data, pageable, count);
    }
}
