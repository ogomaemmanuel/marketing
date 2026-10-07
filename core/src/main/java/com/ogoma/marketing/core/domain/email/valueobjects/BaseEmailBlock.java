package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
        visible = true
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
        @JsonSubTypes.Type(value = HeroBlock.class, name = "hero"),
        @JsonSubTypes.Type(value = HorizontalLayoutBlock.class, name = "horizontal-layout"),
        @JsonSubTypes.Type(value = VerticalLayoutBlock.class, name = "vertical-layout"),
})
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class BaseEmailBlock implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    @NotNull(message = "Block type is required")
    private EmailBlockType type;
    @Pattern(regexp = "^(small|normal|large)$", message = "Invalid block padding")
    private String padding;
    @Pattern(regexp = "^(left|center|right)$", message = "Invalid align value")
    @JsonProperty("align")
    private TextAlignment align = TextAlignment.LEFT;

    public String renderHtml() {
        return renderSection(paddingValue(), cardBackground());
    }

    /** MJML elements valid directly inside an mj-column, with no outer padding of their own. */
    protected abstract String renderContent();

    /** One or more mj-sections; layouts call this to control the outer padding and background. */
    protected String renderSection(String padding, String background) {
        var content = renderContent();
        if (content == null || content.isBlank()) {
            return "";
        }
        return """
                <mj-section background-color="%s" padding="%s">
                  <mj-column padding="0">
                    %s
                  </mj-column>
                </mj-section>
                """.formatted(background, padding, content);
    }

    /** Bare px value (e.g. "16px") driven by the user-configurable padding setting. */
    protected String paddingValue() {
        return switch (this.getPadding()) {
            case "small" -> "8px";
            case "large" -> "24px";
            case null, default -> "16px";
        };
    }

    protected String baseStyle() {
        return "padding: %s;".formatted(paddingValue());
    }

    protected String getPaddingCss(){
        return baseStyle();
    }

    /** Background color for the white "card" every block renders on top of the grey canvas. */
    protected String cardBackground() {
        return EmailTheme.CONTENT_BACKGROUND;
    }

    /** Corner radius for the white "card" every block renders on top of the grey canvas. */
    protected String cardBorderRadius() {
        return EmailTheme.CARD_BORDER_RADIUS;
    }

}
