package com.ogoma.marketing.core.domain.polls.entities;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;


@Table(name = "poll_votes")
public class PollVote extends AggregateRoot<PollVoteID> {
    CampaignID campaignID;
    PollID pollID;
    String feedback;
    Instant votedAt;

    public PollVote() {
        super(null);
    }

    public PollVote(
            CampaignID campaignID,
            PollID pollID,
            String feedback,
            Instant votedAt) {
        super(new PollVoteID());
        this.campaignID = campaignID;
        this.pollID = pollID;
        this.feedback = feedback;
        this.votedAt = votedAt;
    }
}
