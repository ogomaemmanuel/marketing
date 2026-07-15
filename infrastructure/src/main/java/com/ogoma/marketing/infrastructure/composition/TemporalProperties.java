package com.ogoma.marketing.infrastructure.composition;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "marketing.temporal")
public record TemporalProperties(String address, String namespace, String messageSenderQueue) {
}
