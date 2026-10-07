package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.jcputney.mjml.MjmlRenderResult;
import dev.jcputney.mjml.MjmlRenderer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class EmailTemplate implements Serializable {
    @JsonProperty("metadata")
    private EmailTemplateMetaData metaData;
    @JsonProperty("settings")
    private EmailSetting emailSetting;
    private List<BaseEmailBlock> blocks;

    public EmailTemplate() {
        this.blocks = new ArrayList<>();
    }

    public String renderHtml() {
        String blocksHtml = Optional.ofNullable(this.blocks)
                .orElseGet(ArrayList::new)
                .stream()
                .map(BaseEmailBlock::renderHtml)
                .collect(Collectors.joining("\n          "));

        String subject = Optional.ofNullable(emailSetting)
                .map(EmailSetting::getSubject)
                .orElse("");

        String previewText = Optional.ofNullable(emailSetting)
                .map(EmailSetting::getPreviewText)
                .filter(text -> !text.isBlank())
                .map("<mj-preview>%s</mj-preview>"::formatted)
                .orElse("");

        String mjmlString = """
                <mjml>
                  <mj-head>
                    <mj-title>%s</mj-title>
                    %s
                    <mj-attributes>
                      <mj-all font-family="%s" font-size="16px" color="%s" line-height="1.6" />
                      <mj-text font-family="%s" font-size="16px" color="%s" line-height="1.6" />
                    </mj-attributes>
                  </mj-head>
                  <mj-body background-color="%s" width="600px">
                    %s
                  </mj-body>
                </mjml>
                """.formatted(
                subject,
                previewText,
                EmailTheme.FONT_FAMILY,
                EmailTheme.TEXT_COLOR,
                EmailTheme.FONT_FAMILY,
                EmailTheme.TEXT_COLOR,
                EmailTheme.BODY_BACKGROUND,
                blocksHtml
        );
// One-liner with defaults
        MjmlRenderResult result = MjmlRenderer.render(mjmlString);
        return result.html();
    }
}