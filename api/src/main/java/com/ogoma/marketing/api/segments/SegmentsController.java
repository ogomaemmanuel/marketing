package com.ogoma.marketing.api.segments;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/segments")
public record SegmentsController(
        CommandDispatcher commandDispatcher
) {
    @PostMapping
    public ResponseEntity<UUID> createSegment(
            @RequestBody @Valid CreateSegmentRequest createSegmentRequest,
            @CurrentUser String userId, UriComponentsBuilder uriComponentsBuilder) {
        SegmentID segmentID = this.commandDispatcher.dispatch(createSegmentRequest.toCommand(userId));
        URI uri = uriComponentsBuilder.path("/{id}").buildAndExpand(segmentID.id()).toUri();
        return ResponseEntity.created(uri).body(segmentID.id());
    }
}
