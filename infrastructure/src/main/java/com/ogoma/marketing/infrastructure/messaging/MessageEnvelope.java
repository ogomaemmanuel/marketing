package com.ogoma.marketing.infrastructure.messaging;

import java.util.Map;

public record MessageEnvelope<T>(
        String id,

        String eventType,

        String aggregateType,

        String aggregateId,

        String key,

        String destination,

        RoutingType routingType,

        T payload,

        Map<String, String> headers,

        Metadata metadata
) {
}