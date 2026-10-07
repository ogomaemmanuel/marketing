package com.ogoma.marketing.core.domain.email.valueobjects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class DividerBlock extends BaseEmailBlock {
    @Pattern(regexp = "^(dotted|dashed|solid|double)$", message = "Invalid divider style")
    private String style;
    @Min(value = 1, message = "Invalid divider thickness")
    private short thickness;
    private String color;
    private String width;
    private short marginTop;
    private short marginBottom;

    @Override
    public String renderHtml() {
        String borderStyle = (this.getStyle() != null && !this.getStyle().isBlank()) ? this.getStyle() : "solid";
        short borderThickness = this.getThickness() > 0 ? this.getThickness() : 1;
        String borderWidth = (this.getWidth() != null && !this.getWidth().isBlank()) ? this.getWidth() : "100%";
        String borderColor = (this.getColor() != null && !this.getColor().isBlank()) ? this.getColor() : EmailTheme.BORDER_COLOR;
        TextAlignment align = (this.getAlign() != null) ? this.getAlign() : TextAlignment.CENTER;

        return """
                <mj-section background-color="%s" padding="%s">
                <mj-column padding="0">
                <mj-divider border-style="%s"
                            border-width="%dpx"
                            border-color="%s"
                            width="%s;"
                            align="%s"
                            padding-top="%dpx"
                            padding-bottom="%dpx" />
                            </mj-column>
                            </mj-section>
                """.formatted(
                cardBackground(),
                paddingValue(),
                borderStyle,
                borderThickness,
                borderColor,
                borderWidth,
                align.toCss(),
                this.getMarginTop(),
                this.getMarginBottom()
        );
    }
}