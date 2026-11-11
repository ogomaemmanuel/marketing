package com.ogoma.marketing.core.application.polls;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.polls.entities.PollVoteID;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;

import java.util.UUID;

public record SubmitVoteCommand(
         CampaignID campaignID,
         UUID trackingId,
         PollID pollID,
         String feedback
) implements Command<PollVoteID> {


}
