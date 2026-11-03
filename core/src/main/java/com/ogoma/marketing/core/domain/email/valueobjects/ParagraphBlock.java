package com.ogoma.marketing.core.domain.email.valueobjects;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class ParagraphBlock extends BaseEmailBlock {
    private String content;
    private String fontFamily;
    private String fontSize ;
    private String backgroundColor;
    private String color;

    @Override
    protected String renderSection(String padding, String background) {
        return super.renderSection(padding, hasCustomBackground()
                ? EmailTemplateSafety.color(this.getBackgroundColor(), EmailTheme.CONTENT_BACKGROUND) : background);
    }

    @Override
    protected String renderContent() {
        String safeContent = this.getContent() != null ? StringEscapeUtils.escapeHtml4(this.getContent()) : "";
        TextAlignment alignment = (this.getAlign() != null) ? this.getAlign() : TextAlignment.LEFT;
        String textColor = EmailTemplateSafety.color(this.getColor(), EmailTheme.TEXT_COLOR);
        String textFontFamily = "'Helvetica Neue', Helvetica, Arial, sans-serif";
        String textFontSize = EmailTemplateSafety.dimension(this.getFontSize(), "16px");
        String containerBackground = hasCustomBackground()
                ? " container-background-color=\"%s\"".formatted(EmailTemplateSafety.color(this.getBackgroundColor(), EmailTheme.CONTENT_BACKGROUND))
                : "";

        return """
                <mj-text align="%s" color="%s" font-family="%s" font-size="%s" padding="0"%s>
                  %s
                </mj-text>
                """.formatted(
                alignment.toCss(),
                textColor,
                textFontFamily,
                textFontSize,
                containerBackground,
                safeContent
        );
    }

    private boolean hasCustomBackground() {
        return this.getBackgroundColor() != null && !this.getBackgroundColor().isBlank();
    }
}
