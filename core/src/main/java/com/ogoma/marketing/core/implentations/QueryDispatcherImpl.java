package com.ogoma.marketing.core.implentations;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.abstractions.QueryHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryDispatcherImpl implements QueryDispatcher {

    private final Map<Class<? extends Query<?>>, QueryHandler<? extends Query<?>, ?>> queryHandlersRegistry = new ConcurrentHashMap<>();

    public QueryDispatcherImpl(List<QueryHandler<? extends Query<?>, ?>> handlers) {
        for (QueryHandler<? extends Query<?>, ?> handler : handlers
        ) {
            queryHandlersRegistry.put(handler.supports(), handler);
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> R dispatch(Q query) {
        var handler = (QueryHandler<Q, R>) queryHandlersRegistry.get(query.getClass());
        if (handler == null) {
            throw new IllegalArgumentException(String.format("Handler not found for %s", query.getClass().getSimpleName()));
        }
        return handler.handle(query);
    }
}
