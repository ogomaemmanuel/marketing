package com.ogoma.marketing.infrastructure.polls;

import com.ogoma.marketing.core.domain.polls.entities.Poll;
import com.ogoma.marketing.core.domain.polls.repository.PollRepository;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public record PollsRepositoryJDBCAdapter(
        JdbcAggregateTemplate jdbcAggregateTemplate
) implements PollRepository {
    @Override
    public Poll save(Poll poll) {
        return jdbcAggregateTemplate.save(poll);
    }

    @Override
    public Boolean existByID(PollID pollID) {
        return jdbcAggregateTemplate.existsById(pollID, Poll.class);
    }

    @Override
    public Optional<Poll> findByID(PollID pollID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(pollID,Poll.class));
    }

    @Override
    public Boolean existsByName(String name) {
        var criteriaDef = Criteria.where(Poll::getName).is(name.trim()).ignoreCase(true);
        return jdbcAggregateTemplate.exists(Query.query(criteriaDef), Poll.class);
    }
}
