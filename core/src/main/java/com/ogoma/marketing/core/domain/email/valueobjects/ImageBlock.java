package com.ogoma.marketing.core.domain.email.valueobjects;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
        var rawSrc = valueOrEmpty(getSrc());
        // Escape '&' so strict XML SAX parsers don't fail on URL parameters like &w=3096
        var src = rawSrc.replace("&", "&amp;");

        var alt = StringEscapeUtils.escapeXml10(valueOrEmpty(getAlt()));

        // 1. Fix width: MJML defaults to 100% column width when width attribute is omitted.
        // If width is "100%" or empty, don't pass the width attribute.
        var widthAttribute = hasText(getWidth()) && !StringUtils.defaultString(getWidth()).equalsIgnoreCase("100%")
                ? "width=\"%s\"".formatted(getWidth())
                : "";

        var alignment = getAlign() != null
                ? getAlign()
                : TextAlignment.CENTER;

        // 2. Fix height: Do not pass height="auto" as an attribute to mj-image.
        var heightAttribute = hasText(getHeight())
                && !"auto".equalsIgnoreCase(getHeight())
                ? "height=\"%s\"".formatted(getHeight())
                : "";

        return """
                <mj-section background-color="%s" padding="%s">
                <mj-column padding="0">
                <mj-image
                    fluid-on-mobile="true"
                    src="%s"
                    alt="%s"
                    %s
                    %s
                    border-radius="%s"
                    padding="0"
                    />
                    </mj-column>
                </mj-section>
                """.formatted(
                cardBackground(),
                paddingValue(),
                src,
                alt,
                widthAttribute,
                heightAttribute,
                EmailTheme.BORDER_RADIUS
        );
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String valueOrEmpty(String value) {
        return value != null ? value : "";
    }
}