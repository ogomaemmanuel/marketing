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
    public String renderHtml() {
        String safeContent = this.getContent() != null ? StringEscapeUtils.escapeHtml4(this.getContent()) : "";
        return """
                <mj-section>
                  <mj-column>
                    <mj-text>
                      %s
                    </mj-text>
                  </mj-column>
                </mj-section>
                """.formatted(
                safeContent
        );
    }
}