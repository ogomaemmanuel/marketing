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
        String bgColor = "transparent".equals(this.getBackgroundColor())
                ? "transparent"
                : this.getBackgroundColor();
        return """
                <div style="height: %dpx; background-color: %s; width: 100%%;"></div>
                """.formatted(this.getHeight(), bgColor);
    }
}
