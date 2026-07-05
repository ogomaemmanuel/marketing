package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;


public enum EmailBlockType {
    HEADING("heading"),
    PARAGRAPH("paragraph"),
    LIST("list"),
    TABLE("table"),
    BUTTON("button"),
    IMAGE("image"),
    VIDEO("video"),
    SPACER("spacer"),
    DIVIDER("divider"),
    CODE("code"),
    SOCIAL("social-share"),
    RSS("rss"),
    VERTICAL_LAYOUT("vertical-layout"),
    HORIZONTAL_LAYOUT("horizontal-layout");

    private final String value;

    EmailBlockType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static EmailBlockType fromValue(String value) {
        for (EmailBlockType type : EmailBlockType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown block type: " + value);
    }

    @Override
    public String toString() {
        return value;
    }

}
