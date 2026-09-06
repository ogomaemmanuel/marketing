package com.ogoma.marketing.core.sharedkernel.ddd;

import lombok.Getter;
import org.springframework.data.annotation.Id;


public abstract class Entity<ID> {
    @Id
    @Getter
    protected ID id;
    protected Entity(ID id) {
        this.id = id;
    }

}
