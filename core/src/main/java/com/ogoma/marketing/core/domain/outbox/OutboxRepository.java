package com.ogoma.marketing.core.domain.outbox;

import org.springframework.data.domain.Limit;

import java.util.List;

public interface OutboxRepository {

    List<Outbox> saveAll(List<Outbox> outboxes);

    Outbox save(Outbox outbox);

    List<Outbox> findUnProcessed(Limit limit);
}
