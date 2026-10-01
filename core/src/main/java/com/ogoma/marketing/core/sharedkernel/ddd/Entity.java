package com.ogoma.marketing.core.sharedkernel.ddd;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


//@QueryExclude // FIX: Stop Infobip/QueryDSL APT from generating broken QEntity.java
public abstract class Entity<ID extends Serializable> {
    @Id
    @Getter
    protected ID id;

    protected Entity(ID id) {
        this.id = id;
    }

}
