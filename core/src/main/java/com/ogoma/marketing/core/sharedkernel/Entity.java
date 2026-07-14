package com.ogoma.marketing.core.sharedkernel;

public abstract class Entity<ID> {
    protected ID id;

    protected Entity(ID id) {
        this.id = id;
    }

}
