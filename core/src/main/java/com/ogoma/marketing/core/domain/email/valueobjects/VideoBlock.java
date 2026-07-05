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
    public String renderHtml() {
        String heightStyle = !"auto".equals(this.getHeight())
                ? "height: \" + this.getHeight() + \";"
                : "";
        return String.format("""
                        <div style="%s">
                          <video controls="%s" autoplay="%s" style="max-width: 100%%; width: %s; %s">
                            <source src="%s" type="video/mp4">
                            Your email client does not support video playback.
                          </video>
                        </div>""",
                this.baseStyle(), this.getControls(), this.getAutoPlay(),
                this.getWidth(), heightStyle, this.getSrc()
        );
    }
}
