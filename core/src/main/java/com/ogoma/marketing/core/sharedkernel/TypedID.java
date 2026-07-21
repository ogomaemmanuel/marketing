package com.ogoma.marketing.core.sharedkernel;

import java.io.Serializable;

public interface TypedID<T> extends Serializable {
    T id();
}
