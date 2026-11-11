package com.ogoma.marketing.api.polls;


import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.domain.polls.entities.PollVoteID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/votes")
public record VotesController(
        CommandDispatcher commandDispatcher
) {


    @PostMapping
    public PollVoteID submitVote(
            SubmitVoteRequest submitVoteRequest
    ) {
        return this.commandDispatcher.dispatch(submitVoteRequest.toCommand());
    }

}
