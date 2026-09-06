package com.ogoma.marketing.core.sharedkernel.ddd;

import java.io.Serializable;

public interface TypedID<T> extends Serializable {
    T id();
}
