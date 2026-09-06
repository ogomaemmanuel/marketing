package com.ogoma.marketing.campaignsbatchprocessor;

import com.ogoma.marketing.infrastructure.messaging.MessageEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Component
public class CampaignSendEventConsumer {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(CampaignSendEventConsumer.class);

    public CampaignSendEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "marketing-events", groupId = "batch-processing")
    public void consume(@Payload MessageEnvelope<?> messageEnvelope) {
        log.info("consuming campaign sent event {}", messageEnvelope);
        try {
            Class<?> clazz = Class.forName(messageEnvelope.eventType());
            Object domainEvent = objectMapper.convertValue(messageEnvelope.payload(), clazz);
            log.info("Domain event type {}", domainEvent.getClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
