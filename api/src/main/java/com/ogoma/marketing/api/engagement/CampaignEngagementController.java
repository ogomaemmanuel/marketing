package com.ogoma.marketing.api.engagement;


import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.application.engagement.RecordEngagementCommand;
import com.ogoma.marketing.core.domain.engagment.EngagementType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/engagement-tracking")
public class CampaignEngagementController {
    private final CommandDispatcher commandDispatcher;
    // Tiny 1x1 transparent GIF base64 byte array for open tracking
    private static final byte[] TRANSPARENT_PIXEL = java.util.Base64.getDecoder().decode(
            "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7"
    );

    public CampaignEngagementController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }


    @GetMapping("/open/{trackingId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> recordOpenEngagement( @PathVariable UUID trackingId) {
        log.info("Tracking opened link for {}", trackingId);
        CompletableFuture.runAsync(() -> commandDispatcher.dispatch(new RecordEngagementCommand(trackingId,
                        EngagementType.OPEN, "", "")))
                .exceptionally(ex -> {
                            log.error("Failed to record engagement for tracking ID: {}", trackingId, ex);
                            return null; // Prevents the exception from propagating further
                        }
                );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_GIF);
        headers.setCacheControl(CacheControl.noStore().mustRevalidate());
        return new ResponseEntity<>(TRANSPARENT_PIXEL, headers, HttpStatus.OK);
    }

    @GetMapping("/click/{trackingId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> recordClicks(@PathVariable UUID trackingId, @RequestParam("redirect") String target) {
        log.info("Tracking clicks for trackingID {}", trackingId);
        CompletableFuture.runAsync(() -> commandDispatcher.dispatch(new RecordEngagementCommand(trackingId, EngagementType.CLICK, "", "")))
                .exceptionally(ex -> {
                            log.error("Failed to record engagement for tracking ID: {}", trackingId, ex);
                            return null; // Prevents the exception from propagating further
                        }
                );
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(target))
                .build();
    }
}
