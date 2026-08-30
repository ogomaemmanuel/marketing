package com.ogoma.marketing.core.domain.outbox;

import java.util.List;
import java.util.stream.Stream;

public interface OutboxRepository {

    List<Outbox> saveAll(List<Outbox> outboxes);

    Outbox save(Outbox outbox);

    Stream<Outbox> findUnProcessed(Integer limit);
}
