package com.ogoma.marketing.core.domain.email.valueobjects;


import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class ImageBlock extends BaseEmailBlock {

    private String src;

    private String alt;

    private String width;

    private String height;
    private String caption;

    @Override
    public String renderHtml() {

        var imageHeight = this.getHeight().equals("auto") ? "" : """
                height: %s
                """.formatted(this.getHeight());
        var imageCaption = this.caption == null ? "" : """
                <p style="font-size: 14px; color: #6b7280; margin-top: 8px; text-align: center;">%s</p>
                """.formatted(this.getCaption());
        return """
                <div style="%s">
                            <img src="%s" alt="%s" style="max-width: 100%%; height: auto; width: %s; %s" />
                            %s
                          </div>
                """.formatted(this.baseStyle(), this.getSrc(), this.getAlt(), this.getWidth(), imageHeight, imageCaption);

    }
}
