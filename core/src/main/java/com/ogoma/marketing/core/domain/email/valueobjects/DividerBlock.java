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
        return """
                <div style="margin-top: %spx; margin-bottom: %spx; text-align: %s;">
                  <hr style="border: none; border-top: %spx %s %s; width: %s; margin: 0;" />
                </div>
                  """.formatted(
                this.getMarginBottom(),
                this.getMarginBottom(),
                this.getAlign(),
                this.getThickness(),
                this.getStyle(),
                this.getColor(),
                this.getWidth());
    }
}
