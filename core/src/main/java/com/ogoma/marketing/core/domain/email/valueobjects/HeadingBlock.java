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
    protected String renderContent() {
        // Defaults to H1 if not set
        short headingLevel = this.getLevel() != null ? this.getLevel() : 1;

        TextAlignment alignment = (this.getAlign() != null) ? this.getAlign() : TextAlignment.LEFT;

        return """
                <mj-text
                         align="%s"
                         color="%s"
                         font-family="%s"
                         padding="0">
                    <h%d style="margin: 0; font-weight: 700; font-family: inherit; color: %s;">%s</h%d>
                </mj-text>
                """.formatted(
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