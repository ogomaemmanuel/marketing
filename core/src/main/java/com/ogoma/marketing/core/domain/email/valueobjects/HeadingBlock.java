package com.ogoma.marketing.core.domain.email.valueobjects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class HeadingBlock extends BaseEmailBlock {
    private String content;
    @Min(1)
    @Max(6)
    @NotNull
    private Short level;

    @Override
    public String renderHtml() {
        // Defaults to H1 if not set
        short headingLevel = this.getLevel() != null ? this.getLevel() : 1;

        TextAlignment alignment = (this.getAlign() != null) ? this.getAlign() : TextAlignment.LEFT;

        // This wrapper ensures MJML boundary alignment, while allowing HTML block elements to be nested
        return """
                <mj-section background-color="%s" padding="%s">
                <mj-column padding="0">
                <mj-text
                         align="%s"
                         color="%s"
                         font-family="%s"
                         padding="0">
                    <h%d style="margin: 0; font-weight: 700; font-family: inherit; color: %s;">%s</h%d>
                </mj-text>
                </mj-column>
                </mj-section>
                """.formatted(
                cardBackground(),
                paddingValue(),
                alignment.toCss(),
                EmailTheme.HEADING_COLOR,
                EmailTheme.FONT_FAMILY,
                headingLevel,
                EmailTheme.HEADING_COLOR,
                this.getContent(),
                headingLevel
        );
    }
}