package com.ogoma.marketing.infrastructure.composition;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("campaign.engagement.tracking")
public record EngagementTrackingProperties(
    String baseUrl
) {
}
