package com.ogoma.marketing.core.domain.email.valueobjects;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class CodeBlock extends BaseEmailBlock {
    private String content;
    private String language;
    private String showLineNumbers;
    private String backgroundColor;
    private String textColor;
    @Pattern(regexp = "^(small|medium|large)$", message = "Invalid font size")
    private String fontSize;
    @Pattern(regexp = "^(monospace|courier|consolas)$", message = "Invalid font family")
    private String fontFamily;

    @Override
    public String renderHtml() {
        String blockFontFamily = switch (this.getFontFamily()) {
            case "courier" -> "'Courier New', Courier, monospace";
            case "consolas" -> "'Consolas', 'Liberation Mono', Menlo, monospace";
            case null, default -> "monospace";
        };

        String blockFontSize = switch (this.getFontSize()) {
            case "small" -> "12px";
            case "large" -> "16px";
            case null, default -> "14px";
        };

        String bgColor = (this.getBackgroundColor() != null && !this.getBackgroundColor().isBlank())
                ? this.getBackgroundColor()
                : "#1e293b";

        String txtColor = (this.getTextColor() != null && !this.getTextColor().isBlank())
                ? this.getTextColor()
                : "#f8fafc";

        String safeContent = this.getContent() != null ? StringEscapeUtils.escapeHtml4(this.getContent()) : "";
        return """
                <mj-section background-color="%s" border-radius="%s" padding="%s">
                  <mj-column padding="0">
                    <mj-text padding="0" color="%s" font-family="%s" font-size="%s">
                      <pre style="margin: 0; white-space: pre-wrap; word-break: break-word; font-family: %s; font-size: %s; color: %s;">%s</pre>
                    </mj-text>
                  </mj-column>
                </mj-section>
                """.formatted(
                bgColor,
                EmailTheme.BORDER_RADIUS,
                paddingValue(),
                txtColor,
                blockFontFamily,
                blockFontSize,
                blockFontFamily,
                blockFontSize,
                txtColor,
                safeContent
        );
    }
}
