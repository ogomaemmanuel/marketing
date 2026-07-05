package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible=true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ButtonBlock.class, name = "button"),
        @JsonSubTypes.Type(value = CodeBlock.class, name = "code"),
        @JsonSubTypes.Type(value = DividerBlock.class, name = "divider"),
        @JsonSubTypes.Type(value = ParagraphBlock.class, name = "paragraph"),
        @JsonSubTypes.Type(value = HeadingBlock.class, name = "heading"),
        @JsonSubTypes.Type(value = ImageBlock.class, name = "image"),
        @JsonSubTypes.Type(value = ListBlock.class, name = "list"),
        @JsonSubTypes.Type(value = SpacerBlock.class, name = "spacer"),
        @JsonSubTypes.Type(value = TableBlock.class, name = "table"),
        @JsonSubTypes.Type(value = VideoBlock.class, name = "video"),
})
public abstract class BaseEmailBlock implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    @NotNull(message = "Block type is required")
    private EmailBlockType type;
    @Pattern(regexp = "^(small|normal|large)$", message = "Invalid block padding")
    private String padding;
    @Pattern(regexp = "^(left|center|right)$", message = "Invalid align value")
    private String align;

    public abstract String renderHtml();

    protected String baseStyle() {
        var a = switch (this.getAlign()) {
            case "center" -> "text-align: center;";
            case "right" -> "text-align: right;";
            case null, default -> "text-align: left;";
        };
        var p = switch (this.getPadding()) {
            case "small" -> "padding: 8px;";
            case "large" -> "padding: 24px;";
            case null, default -> "padding: 16px;";
        };
        return String.format("%s %s", p, a);
    }
}
