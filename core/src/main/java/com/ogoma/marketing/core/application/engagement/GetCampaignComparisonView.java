package com.ogoma.marketing.core.application.engagement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetCampaignComparisonView(
        List<CampaignPerformanceDto> campaigns
) {

    public record CampaignPerformanceDto(
            String campaignId,
            String campaignName,
            long deliveries,
            long opens,
            long clicks
    ) {

        @JsonProperty
        public double clickRate() {
            return deliveries == 0 ? 0.0 : (double) clicks / deliveries;
        }

        @JsonProperty
        public double openRate() {
            return opens == 0 ? 0.0 : (double) opens / deliveries;
        }
    }
}
