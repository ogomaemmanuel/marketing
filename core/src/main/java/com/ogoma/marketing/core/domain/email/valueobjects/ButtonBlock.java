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
        // Base style
        String baseButtonStyle = """
                display: inline-block;
                text-decoration: none;
                border-radius: 4px;
                font-weight: 500;
                text-align: center;
                """;

        // Size styles
        String sizeStyle = switch (this.getSize()) {
            case "small" -> "padding: 4px 12px; font-size: 14px;";
            case "large" -> "padding: 12px 24px; font-size: 18px;";
            case null, default -> "padding: 8px 16px; font-size: 16px;";
        };

        // Variant styles
        String variantStyle = switch (this.getVariant()) {
            case "primary" -> "background-color: #3b82f6; color: white;";
            case "secondary" -> "background-color: #6b7280; color: white;";
            case null, default -> "border: 1px solid #d1d5db; background-color: white; color: #374151;";
        };

        String finalButtonStyle = baseButtonStyle + " " + sizeStyle + " " + variantStyle;

        return """
                <div style="%s">
                    <a href="%s" style="%s">%s</a>
                </div>
                """.formatted(
                baseStyle(),
                this.getUrl(),
                finalButtonStyle,
                this.getText()
        );
    }

}
