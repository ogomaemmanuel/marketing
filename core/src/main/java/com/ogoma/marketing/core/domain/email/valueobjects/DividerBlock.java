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
    private Short thickness;
    private String color;
    private String width;
    private short marginTop;
    private short marginBottom;

    @Override
    protected String renderContent() {
        String borderStyle = switch (this.getStyle() == null ? "solid" : this.getStyle()) {
            case "dotted", "dashed", "double" -> this.getStyle();
            default -> "solid";
        };
        short borderThickness = this.getThickness() > 0 ? this.getThickness() : 1;
        String borderWidth = EmailTemplateSafety.dimension(this.getWidth(), "100%");
        String borderColor = EmailTemplateSafety.color(this.getColor(), EmailTheme.BORDER_COLOR);
        TextAlignment align = (this.getAlign() != null) ? this.getAlign() : TextAlignment.CENTER;

        return """
                <mj-divider border-style="%s"
                            border-width="%dpx"
                            border-color="%s"
                            width="%s"
                            align="%s"
                            padding-left="0"
                            padding-right="0"
                            padding-top="%dpx"
                            padding-bottom="%dpx" />
                """.formatted(
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
