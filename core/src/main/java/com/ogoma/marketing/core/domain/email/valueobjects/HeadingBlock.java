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
    private String padding;
    private String align;
    @Min(1)
    @Max(6)
    @NotNull
    private Short level;
    @Override
    public String renderHtml() {
        return """
                <h%s style="%s font-weight: bold; margin: 0;">%s</h%s>
                 """.formatted(this.getLevel(), this.baseStyle(), this.getContent(), this.getLevel());
    }
}
