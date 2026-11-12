package com.ogoma.marketing.api.polls;

import com.ogoma.marketing.core.application.polls.SubmitVoteCommand;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;

import java.util.UUID;

public record SubmitVoteRequest(
        UUID campaignId,
        UUID pollId,
        UUID trackingId,
        String feedback
) {

    SubmitVoteCommand toCommand(){
        return  new SubmitVoteCommand(null,trackingId,new PollID(pollId),feedback);
    }
}
