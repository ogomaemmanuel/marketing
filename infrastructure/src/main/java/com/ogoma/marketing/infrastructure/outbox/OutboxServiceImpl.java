package com.ogoma.marketing.infrastructure.outbox;

import com.ogoma.marketing.core.abstractions.OutboxService;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.outbox.Outbox;
import com.ogoma.marketing.core.domain.outbox.OutboxRepository;
import com.ogoma.marketing.infrastructure.composition.OutboxProperties;
import com.ogoma.marketing.infrastructure.messaging.EventBus;
import com.ogoma.marketing.infrastructure.messaging.MessageEnvelope;
import com.ogoma.marketing.infrastructure.messaging.RoutingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Clock;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


@Component
public class OutboxServiceImpl implements OutboxService {
    private final OutboxRepository outboxRepository;
    private final OutboxProperties outboxProperties;
    private final EventBus eventBus;
    private final Clock clock;
    private final UnitOfWork unitOfWork;

    private static final Logger log = LoggerFactory.getLogger(OutboxServiceImpl.class);

    public OutboxServiceImpl(OutboxRepository outboxRepository, OutboxProperties outboxProperties, EventBus eventBus, Clock clock, UnitOfWork unitOfWork) {
        this.outboxRepository = outboxRepository;
        this.outboxProperties = outboxProperties;
        this.eventBus = eventBus;
        this.clock = clock;
        this.unitOfWork = unitOfWork;
    }

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    @Override
    public void processPendingMessages() {
        unitOfWork.execute(() -> {
            try (Stream<Outbox> outboxStream = this.outboxRepository.findUnProcessed(outboxProperties.getOutboxMessageLimit())) {
                outboxStream.forEach(outbox -> {
                    MessageEnvelope<Serializable> envelope =
                            new MessageEnvelope<>(
                                    outbox.getId().id().toString(),
                                    outbox.getEventType(),
                                    outbox.getAggregateType(),
                                    outbox.getAggregateID().toString(),
                                    outbox.getAggregateID().toString(),
                                    "marketing-events",
                                    RoutingType.TOPIC,
                                    outbox.getPayload().value(),
                                    new HashMap<>(),
                                    null
                            );
                    eventBus.publish(envelope);
                    outbox.markAsProcessed(clock);
                    this.outboxRepository.save(outbox);
                    log.info("Processing outbox {}", outbox);
                });

            }
        });

    }


}
