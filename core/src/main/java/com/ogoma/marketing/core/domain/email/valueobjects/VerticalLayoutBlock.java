package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerticalLayoutBlock extends BaseEmailBlock {
    private Gap gap;
    private String backgroundColor;
    private Integer borderRadius;
    private Integer borderWidth;
    private String borderColor;
    private BorderStyle borderStyle;
    private List<BaseEmailBlock> children;

    @Override
    public String renderHtml() {
        var blocks = children == null
                ? List.<BaseEmailBlock>of()
                : children.stream().filter(Objects::nonNull).toList();

        if (blocks.isEmpty()) {
            return "";
        }

        var background = hasText(backgroundColor) ? backgroundColor : cardBackground();
        // Children paint their own section background, so they must inherit the layout's color.
        blocks.forEach(block -> block.setContainerBackground(background));

        var gapPixels = (gap == null ? Gap.NONE : gap).pixels();
        var separator = gapPixels > 0
                ? "\n" + """
                <mj-section padding="0">
                <mj-column padding="0">
                <mj-spacer height="%dpx" />
                </mj-column>
                </mj-section>
                """.formatted(gapPixels)
                : "\n";

        var childrenMjml = blocks.stream()
                .map(BaseEmailBlock::renderHtml)
                .filter(html -> html != null && !html.isBlank())
                .collect(Collectors.joining(separator));

        var attrs = new LinkedHashMap<String, String>();
        attrs.put("padding", paddingValue());
        attrs.put("background-color", background);
        if (borderRadius != null && borderRadius > 0) {
            attrs.put("border-radius", borderRadius + "px");
        }
        if (borderWidth != null && borderWidth > 0) {
            var style = borderStyle == null ? BorderStyle.SOLID : borderStyle;
            var color = hasText(borderColor) ? borderColor : EmailTheme.BORDER_COLOR;
            attrs.put("border", "%dpx %s %s".formatted(borderWidth, style.cssValue(), color));
        }

        return """
                <mj-wrapper%s>
                %s
                </mj-wrapper>""".formatted(attributes(attrs), childrenMjml);
    }

    private static String attributes(Map<String, String> attrs) {
        return attrs.entrySet().stream()
                .map(e -> " %s=\"%s\"".formatted(e.getKey(), escape(e.getValue())))
                .collect(Collectors.joining());
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    public enum Gap {
        NONE(0), SMALL(8), MEDIUM(16), LARGE(24);

        private final int pixels;

        Gap(int pixels) {
            this.pixels = pixels;
        }

        int pixels() {
            return pixels;
        }

        @JsonCreator
        public static Gap fromString(String value) {
            return Gap.valueOf(value.toUpperCase());
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    public enum BorderStyle {
        SOLID, DASHED, DOTTED;

        String cssValue() {
            return name().toLowerCase();
        }

        @JsonCreator
        public static BorderStyle fromString(String value) {
            return BorderStyle.valueOf(value.toUpperCase());
        }
    }
}
