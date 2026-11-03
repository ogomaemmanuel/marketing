package com.ogoma.marketing.infrastructure.composition;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(EngagementTrackingProperties.class)
@Configuration
public class EngagementTrackingConfig {
}
