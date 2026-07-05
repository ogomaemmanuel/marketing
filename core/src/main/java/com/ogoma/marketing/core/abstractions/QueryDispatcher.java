package com.ogoma.marketing.core.abstractions;

public interface QueryDispatcher {

    <Q extends Query<R>,R> R dispatch(Q query);
}
