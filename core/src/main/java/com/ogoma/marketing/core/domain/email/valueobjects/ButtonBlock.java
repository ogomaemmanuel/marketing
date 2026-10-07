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

        // Variant mapping
        String bgColor;
        String color;
        String border = "none";

        switch (this.getVariant() != null ? this.getVariant() : "") {
            case "destructive" -> {
                bgColor = "#ef4444";
                color = "#ffffff";
            }
            case "outline" -> {
                bgColor = "transparent";
                color = "#374151";
                border = "1px solid #d1d5db";
            }
            case "secondary" -> {
                bgColor = "#6b7280";
                color = "#ffffff";
            }
            case "ghost" -> {
                bgColor = "transparent";
                color = "#374151";
            }
            case "link" -> {
                bgColor = "transparent";
                color = "#3b82f6";
            }
            default -> { // "default" or fallback
                bgColor = "#3b82f6";
                color = "#ffffff";
            }
        }

        String targetUrl = this.getUrl() != null ? this.getUrl() : "#";
        String buttonText = this.getText() != null ? this.getText() : "";
        TextAlignment alignment = (this.getAlign() != null) ? this.getAlign() : TextAlignment.CENTER;

        return """
                <mj-section>
                <mj-column>
                <mj-button href="%s"
                           background-color="%s"
                           color="%s"
                           border="%s"
                           font-size="%s"
                           inner-padding="%s"
                           align="%s"
                           border-radius="4px"
                           font-weight="500"
                           style="%s">
                    %s
                </mj-button>
                </mj-column>
                </mj-section>
                """.formatted(
                targetUrl,
                bgColor,
                color,
                border,
                fontSize,
                innerPadding,
                alignment,
                this.baseStyle(),
                buttonText
        );
    }
}