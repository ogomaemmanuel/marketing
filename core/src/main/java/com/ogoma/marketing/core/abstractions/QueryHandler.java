package com.ogoma.marketing.core.abstractions;

import org.jspecify.annotations.NonNull;

public interface QueryHandler<Q extends Query<R>, R> {

    @NonNull Class<Q> supports();

    R handle(@NonNull Q query);
}
