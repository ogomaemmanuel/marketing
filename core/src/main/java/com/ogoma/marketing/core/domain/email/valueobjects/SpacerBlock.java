package com.ogoma.marketing.core.domain.email.valueobjects;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class SpacerBlock extends BaseEmailBlock {

    private Short height;

    private Boolean showBorder;

    private String backgroundColor;

    @Override
    public String renderHtml() {
        // Spacers separate cards, so they sit on the canvas rather than inside a padded card.
        return renderSection("0", "transparent");
    }

    @Override
    protected String renderContent() {
        int heightVal = (this.getHeight() != null && this.getHeight() > 0) ? this.getHeight() : 20;
        String bgColor = (this.getBackgroundColor() != null && !"transparent".equals(this.getBackgroundColor()))
                ? this.getBackgroundColor()
                : "transparent";

        return """
                <mj-spacer height="%dpx" container-background-color="%s" />
                """.formatted(heightVal, bgColor);
    }
}