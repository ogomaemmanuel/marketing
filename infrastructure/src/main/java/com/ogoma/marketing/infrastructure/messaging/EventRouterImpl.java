package com.ogoma.marketing.infrastructure.messaging;

import com.ogoma.marketing.core.abstractions.EventHandler;
import com.ogoma.marketing.core.abstractions.EventRouter;
import com.ogoma.marketing.core.sharedkernel.ddd.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventRouterImpl implements EventRouter {

    private static final Logger log = LoggerFactory.getLogger(EventRouterImpl.class);
    private final Map<Class<? extends DomainEvent>, List<EventHandler<DomainEvent>>> eventHandlersRegistry = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public EventRouterImpl(List<? extends EventHandler<? extends DomainEvent>> eventHandlers) {
        for (EventHandler<? extends DomainEvent> eventHandler : eventHandlers
        ) {
            this.eventHandlersRegistry.computeIfAbsent(eventHandler.supports(),
                            k -> new ArrayList<>())
                    .add((EventHandler<DomainEvent>) eventHandler);
        }
    }


    @Override
    public void route(DomainEvent event) {
        if (event == null) {
            log.warn("Submitted a null event");
            return;
        }
        log.info("Routing event {}", event);
        this.eventHandlersRegistry.computeIfAbsent(event.getClass(),
                _ -> new ArrayList<>()).forEach(handler -> {
            handler.handle(event);
        });
    }
}
