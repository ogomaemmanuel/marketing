package com.ogoma.marketing.core.domain.polls.repository;

import com.ogoma.marketing.core.domain.polls.entities.Poll;

public interface PollRepository {
    Poll save(Poll poll);
    Boolean existsByName(String name);
}
