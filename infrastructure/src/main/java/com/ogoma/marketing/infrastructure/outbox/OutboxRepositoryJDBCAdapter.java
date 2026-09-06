package com.ogoma.marketing.infrastructure.outbox;

import com.ogoma.marketing.core.domain.outbox.Outbox;
import com.ogoma.marketing.core.domain.outbox.OutboxRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;


@Repository
public record OutboxRepositoryJDBCAdapter(JdbcAggregateTemplate jdbcAggregateTemplate) implements OutboxRepository {
    @Override
    public List<Outbox> saveAll(List<Outbox> outboxes) {
        return jdbcAggregateTemplate.saveAll(outboxes);
    }

    @Override
    public Outbox save(Outbox outbox) {
        return jdbcAggregateTemplate.save(outbox);
    }

    @Override
    public Stream<Outbox> findUnProcessed(Integer limit) {
        Criteria criteria = Criteria.where(Outbox::isProcessed).is(false);
        Query query = Query.query(criteria).limit(limit).sort(Sort.by(Sort.Direction.DESC,Outbox::getCreatedAt));
        return jdbcAggregateTemplate.streamAll(query, Outbox.class);
    }
}
