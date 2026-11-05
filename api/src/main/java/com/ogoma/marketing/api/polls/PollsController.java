package com.ogoma.marketing.api.polls;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/polls")
public record PollsController(
        CommandDispatcher commandDispatcher
) {
    @PostMapping
    public ResponseEntity<UUID> createPoll(UriComponentsBuilder uriComponentsBuilder,
                                           @RequestBody @Valid CreatePollRequest createPollRequest,
                                           @CurrentUser String user) {
        PollID pollID = commandDispatcher.dispatch(createPollRequest.toCommand(user));
        return ResponseEntity.created(uriComponentsBuilder.path("/{id}")
                        .build().toUri())
                .body(pollID.id());
    }
}
