package com.ogoma.marketing.infrastructure.configs;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "marketing.temporal")
public record TemporalProperties(String address, String namespace, String messageSenderQueue) {
}
