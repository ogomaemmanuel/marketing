package com.ogoma.marketing.core.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class",
        visible = true
)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Message {
}
