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

        // Apply a base margin-bottom reset on the MJML text tag,
        // while setting padding-left/right to 0px to force boundary alignment.
        TextAlignment alignment = (this.getAlign() != null) ? this.getAlign() : TextAlignment.LEFT;

        // This wrapper ensures MJML boundary alignment, while allowing HTML block elements to be nested
        return """
                <mj-section>
                <mj-column>
                <mj-text
                         align="%s"
                         style="%s">
                    <h%d style="margin: 0; font-weight: bold; font-family: inherit;">%s</h%d>
                </mj-text>
                </mj-column>
                </mj-section>
                """.formatted(
                alignment.toCss(),
                this.baseStyle(), // Standard styles (font-size, color) passed through
                headingLevel,
                this.getContent(),
                headingLevel
        );
    }
}