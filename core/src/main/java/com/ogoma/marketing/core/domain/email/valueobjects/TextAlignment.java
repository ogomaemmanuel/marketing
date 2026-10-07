package com.ogoma.marketing.core.domain.email.valueobjects;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Locale;
@JsonFormat(shape = JsonFormat.Shape.STRING, with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public enum TextAlignment {
    LEFT,
    CENTER,
    RIGHT;
    public String toCss() {
        return name().toLowerCase(Locale.ROOT);
    }
    @JsonCreator
    public static TextAlignment fromString(String value) {
        return TextAlignment.valueOf(value.toUpperCase());
    }
}
