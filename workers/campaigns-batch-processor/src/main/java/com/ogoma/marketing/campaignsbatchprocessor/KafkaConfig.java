package com.ogoma.marketing.campaignsbatchprocessor;

import com.ogoma.marketing.infrastructure.messaging.MessageEnvelope;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, MessageEnvelope<?>> consumerFactory(KafkaProperties kafkaProperties) {
        JacksonJsonDeserializer<MessageEnvelope<?>> jsonDeserializer = new JacksonJsonDeserializer<>(MessageEnvelope.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(true);

        ErrorHandlingDeserializer<MessageEnvelope<?>> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(jsonDeserializer);

        // Build consumer properties map using KafkaProperties from application.yml
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageEnvelope<?>> kafkaListenerContainerFactory(
            ConsumerFactory<String, MessageEnvelope<?>> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, MessageEnvelope<?>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}