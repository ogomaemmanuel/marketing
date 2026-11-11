package com.ogoma.marketing.core.domain.polls.repository;

import com.ogoma.marketing.core.domain.polls.entities.Poll;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;

import java.util.Optional;

public interface PollRepository {
    Poll save(Poll poll);

    Boolean existByID(PollID pollID);
    Optional<Poll> findByID(PollID pollID);
    Boolean existsByName(String name);
}
