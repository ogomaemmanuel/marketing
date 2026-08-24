package com.ogoma.marketing.api.campaigns;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.campaign.commands.SendCampaignCommand;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignByIDQuery;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignByIDView;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignsQuery;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignsView;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
public record CampaignsController(
        QueryDispatcher queryDispatcher,
        CommandDispatcher commandDispatcher) {


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

    @GetMapping(value = "/{id}")
    public GetCampaignByIDView getCampaignByID(
            @PathVariable @Parameter(schema = @Schema(type = "string", format = "uuid")) CampaignID id
    ) {
        return this.queryDispatcher.dispatch(new GetCampaignByIDQuery(id));
    }

    @GetMapping
    public PagedModel<GetCampaignsView> getCampaigns(@RequestParam(required = false) String searchTerm, Pageable pageable) {
        return new PagedModel<>(this.queryDispatcher.dispatch(new GetCampaignsQuery(searchTerm, pageable)));
    }

    @PostMapping("/{id}/actions/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void sendCampaign(@PathVariable @Parameter(schema = @Schema(type = "string", format = "uuid")) CampaignID id, @CurrentUser String userId) {
        return this.commandDispatcher.dispatch(new SendCampaignCommand(id, userId));
    }
}
