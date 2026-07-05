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
            case "courier" -> "'Courier New', monospace";
            case "consolas" -> "'Consolas', monospace";
            case null, default -> "monospace";
        };
        String blockFontSize = switch (this.getFontSize()) {
            case "small" -> "12px";
            case "large" -> "16px";
            case null, default -> "14px";
        };
        return """
                <div style="%s">
                    <div style="background-color: %s; color: %s; padding: 16px; border-radius: 4px; font-family: %s; font-size: %s; overflow-x: auto;">
                        <pre style="margin: 0; white-space: pre-wrap;">%s</pre>
                    </div>
                </div>
                """.formatted(
                baseStyle(),
                this.getBackgroundColor(),
                this.getTextColor(),
                blockFontFamily,
                blockFontSize,
                this.getContent()
        );
    }
}
