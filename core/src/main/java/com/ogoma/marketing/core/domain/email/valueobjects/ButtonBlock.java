package com.ogoma.marketing.core.domain.email.valueobjects;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
public class ButtonBlock extends BaseEmailBlock {
    private String text;
    private String url;
    @Pattern(regexp = "^(default|destructive|outline|secondary|ghost|link)$", message = "Invalid button variant")
    private String variant;
    @Pattern(regexp = "^(small|medium|large)")
    private String size;

    @Override
    public String renderHtml() {
        // Size mapping
        String fontSize = switch (this.getSize() != null ? this.getSize() : "") {
            case "small" -> "14px";
            case "large" -> "18px";
            default -> "16px";
        };

        String innerPadding = switch (this.getSize() != null ? this.getSize() : "") {
            case "small" -> "4px 12px";
            case "large" -> "12px 24px";
            default -> "8px 16px";
        };

        // Variant mapping (Mailchimp-style: signature yellow primary, dark secondary)
        String bgColor;
        String color;
        String border = "none";

        switch (this.getVariant() != null ? this.getVariant() : "") {
            case "destructive" -> {
                bgColor = EmailTheme.DESTRUCTIVE_COLOR;
                color = EmailTheme.DESTRUCTIVE_TEXT_COLOR;
            }
            case "outline" -> {
                bgColor = "transparent";
                color = EmailTheme.TEXT_COLOR;
                border = "1px solid %s".formatted(EmailTheme.TEXT_COLOR);
            }
            case "secondary" -> {
                bgColor = EmailTheme.DARK_COLOR;
                color = EmailTheme.DARK_TEXT_COLOR;
            }
            case "ghost" -> {
                bgColor = "transparent";
                color = EmailTheme.TEXT_COLOR;
            }
            case "link" -> {
                bgColor = "transparent";
                color = EmailTheme.LINK_COLOR;
            }
            default -> { // "default" or fallback
                bgColor = EmailTheme.ACCENT_COLOR;
                color = EmailTheme.ACCENT_TEXT_COLOR;
            }
        }

        String targetUrl = this.getUrl() != null ? this.getUrl() : "#";
        String buttonText = this.getText() != null ? this.getText() : "";
        TextAlignment alignment = (this.getAlign() != null) ? this.getAlign() : TextAlignment.CENTER;

        return """
                <mj-section background-color="%s" padding="%s">
                <mj-column padding="0">
                <mj-button href="%s"
                           background-color="%s"
                           color="%s"
                           border="%s"
                           font-size="%s"
                           inner-padding="%s"
                           align="%s"
                           border-radius="%s"
                           font-weight="500"
                           padding="0">
                    %s
                </mj-button>
                </mj-column>
                </mj-section>
                """.formatted(
                cardBackground(),
                paddingValue(),
                targetUrl,
                bgColor,
                color,
                border,
                fontSize,
                innerPadding,
                alignment,
                EmailTheme.BORDER_RADIUS,
                buttonText
        );
    }
}