package com.ogoma.marketing.infrastructure.messaging.kafka;

import com.ogoma.marketing.infrastructure.messaging.EventBus;
import com.ogoma.marketing.infrastructure.messaging.MessageEnvelope;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaEventBus implements EventBus {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventBus(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void publish(MessageEnvelope<?> message) {
        var producerRecord = new ProducerRecord<>(
                message.destination(),
                message.aggregateId(),
                JsonMapper.shared().writeValueAsString(message)
        );
        this.kafkaTemplate.send(producerRecord);
    }

    @Override
    public CompletableFuture<Void> publishAsync(MessageEnvelope<?> message) {
        return null;
    }

    @Override
    public void publishBatch(List<MessageEnvelope<?>> messages) {

    }
}
