package com.ogoma.marketing.core.domain.polls.repository;

import com.ogoma.marketing.core.domain.polls.entities.PollVote;

import java.util.UUID;

public interface PollVoteRepository {
    PollVote save(PollVote pollVote);

    Boolean existByTrackingId(UUID trackingId);
}
