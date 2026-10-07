package com.ogoma.marketing.core.domain.email.valueobjects;


import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class VideoBlock extends BaseEmailBlock {

    private String height;

    private String src;
    private String width;
    private Boolean controls;
    private Boolean autoPlay;

    @Override
    protected String renderContent() {
        String heightStyle = (this.getHeight() != null && !"auto".equals(this.getHeight()))
                ? "height: %s;".formatted(this.getHeight())
                : "";
        return """
                <mj-text padding="0">
                  <div style="border-radius: %s; overflow: hidden;">
                    <video controls="%s" autoplay="%s" style="max-width: 100%%; width: %s; %s">
                      <source src="%s" type="video/mp4">
                      Your email client does not support video playback.
                    </video>
                  </div>
                </mj-text>
                """.formatted(
                EmailTheme.BORDER_RADIUS,
                this.getControls(), this.getAutoPlay(),
                this.getWidth(), heightStyle, this.getSrc()
        );
    }
}
