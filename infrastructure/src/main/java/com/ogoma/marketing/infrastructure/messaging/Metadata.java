package com.ogoma.marketing.infrastructure.messaging;

import java.time.Instant;
import java.util.Map;

public record Metadata(

        Instant createdAt,

        String correlationId,

        String causationId,

        String traceId,

        String spanId,

        Integer priority,

        Integer deliveryDelaySeconds,

        Long ttlMillis,

        Integer retryCount,

        String orderingKey,

        String partitionKey,

        String contentType,

        String schemaVersion,

        Map<String, String> attributes
) {
}