package com.ogoma.marketing.infrastructure.audience;

import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public record AudienceRepositoryJDBCAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate,
        AudienceJDBCRepository audienceJDBCRepository
) implements AudienceRepository {
    @Override
    public AudienceEntity save(AudienceEntity audienceEntity) {
        return this.jdbcAggregateTemplate.save(audienceEntity);
    }

    @Override
    public Optional<AudienceEntity> findById(AudienceId audienceID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(audienceID, AudienceEntity.class));
    }

    @Override
    public Set<AudienceId> findStaticAudienceIds(Collection<AudienceId> audienceIds) {
        return audienceJDBCRepository.findAllByIdIn(audienceIds).stream().map(AudienceIDOnly::id).collect(Collectors.toSet());
    }

    @Override
    public Page<AudienceEntity> findAllBy(String searchTerm, Pageable pageable) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.hasText(searchTerm)) {
            criteria = Criteria.where(PropertyPath.of(AudienceEntity::getName)).like("%" + searchTerm + "%").ignoreCase(true);
        }
        var listQuery = Query.query(criteria).with(pageable);
        var countQuery = Query.query(criteria);
        var count = jdbcAggregateTemplate.count(countQuery, AudienceEntity.class);
        if (count == 0L) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0L);
        }
        var data = jdbcAggregateTemplate.findAll(listQuery, AudienceEntity.class);
        return new PageImpl<>(data, pageable, count);
    }
}
