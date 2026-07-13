package com.ogoma.marketing.core.customattributtes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "type",
        use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = NumberAttribute.class, name = "number"),
        @JsonSubTypes.Type(value = BooleanAttribute.class, name = "boolean"),
        @JsonSubTypes.Type(value = SelectionAttribute.class, name = "selection"),
        @JsonSubTypes.Type(value = TextAttribute.class, name = "text")
})
public abstract class CustomAttributeBase {
    private UUID id;
    private String label;
    private String type;
}
