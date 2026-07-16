package com.ogoma.marketing.api.campaigns;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
public record CampaignsController(CommandDispatcher commandDispatcher) {


    @PostMapping
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<UUID> createCampaign(
            @Valid @RequestBody CreateCampaignRequest createCampaignRequest,
            @CurrentUser String userId,
            UriComponentsBuilder uriComponentsBuilder) {
        CampaignID campaignID = this.commandDispatcher.dispatch(createCampaignRequest.toCommand(userId));
        URI uri = uriComponentsBuilder.path("/{id}")
                .buildAndExpand(campaignID.id()).toUri();
        return ResponseEntity.created(uri).body(campaignID.id());
    }
}
