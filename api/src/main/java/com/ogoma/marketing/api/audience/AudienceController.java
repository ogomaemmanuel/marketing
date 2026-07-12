package com.ogoma.marketing.api.audience;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.audience.queries.GetAudienceByIDQuery;
import com.ogoma.marketing.core.application.audience.queries.GetAudienceByIDView;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audiences")
public record AudienceController(
        CommandDispatcher commandDispatcher,
        QueryDispatcher queryDispatcher) {
    private static final String BASE_PATH = "/api/v1/audiences";

    @PostMapping
    public ResponseEntity<AddAudienceResponse> createAudience(
            @RequestBody @Valid final AddAudienceRequest audienceRequest,
            @CurrentUser final String userId,
            UriComponentsBuilder uriComponentsBuilder) {
        AudienceId audienceId = this.commandDispatcher.dispatch(audienceRequest.toCommand(userId)).getId();
        URI location = uriComponentsBuilder
                .path(BASE_PATH + "/{id}")
                .buildAndExpand(audienceId.id())
                .toUri();
        return ResponseEntity.created(location).body(new AddAudienceResponse(audienceId));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void updateAudience(@PathVariable UUID id, @RequestBody @Valid UpdateAudienceRequest updateAudienceRequest, @CurrentUser String userId) {
        return this.commandDispatcher.dispatch(updateAudienceRequest.toCommand(new AudienceId(id), userId));
    }

    @GetMapping(value = "/{id}")
    public GetAudienceByIDView getAudienceByID(@PathVariable UUID id) {
        return queryDispatcher.dispatch(new GetAudienceByIDQuery(new AudienceId(id)));
    }


}
