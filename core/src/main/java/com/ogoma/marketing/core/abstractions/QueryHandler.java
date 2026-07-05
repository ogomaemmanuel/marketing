package com.ogoma.marketing.core.abstractions;

public interface QueryHandler<Q extends  Query<R>,R >{

    Class<Q> supports();
    R handle(Q query);
}
