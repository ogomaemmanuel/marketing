package com.ogoma.marketing.core.domain.polls.entities;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;
import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;


@Table(name = "poll_votes")
@Getter
public class PollVote extends AggregateRoot<PollVoteID> {
    private CampaignID campaignID;
    private PollID pollID;
    private String feedback;
    private Instant votedAt;
    private UUID trackingId;

    public PollVote() {
        super(null);
    }

    public PollVote(
            CampaignID campaignID,
            PollID pollID,
            String feedback,
            UUID trackingId,
            Clock clock) {
        super(new PollVoteID());
        this.campaignID = campaignID;
        this.pollID = pollID;
        this.feedback = feedback;
        this.trackingId = trackingId;
        this.votedAt = clock.instant();
    }
}
