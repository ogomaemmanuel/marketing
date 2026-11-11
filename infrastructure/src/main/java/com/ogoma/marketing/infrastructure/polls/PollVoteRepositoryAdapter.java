package com.ogoma.marketing.infrastructure.polls;

import com.ogoma.marketing.core.domain.polls.entities.PollVote;
import com.ogoma.marketing.core.domain.polls.repository.PollVoteRepository;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public record PollVoteRepositoryAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate
) implements PollVoteRepository {
    @Override
    public PollVote save(PollVote pollVote) {
        return jdbcAggregateTemplate.save(pollVote);
    }

    @Override
    public Boolean existByTrackingId(UUID trackingId) {
        return jdbcAggregateTemplate.exists(Query.query(Criteria.where(PollVote::getTrackingId).is(trackingId)), PollVote.class);
    }
}
