package com.ogoma.marketing.core.domain.email.valueobjects;



import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
class ParagraphBlock extends BaseEmailBlock {
    private String content;

    @Override
    public String renderHtml() {
        return """
               <p style="%s margin: 0;">%s</p>
                """.formatted(this.baseStyle(), this.getContent());
    }

}
