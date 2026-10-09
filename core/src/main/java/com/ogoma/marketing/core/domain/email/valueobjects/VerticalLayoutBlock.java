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
import java.util.stream.IntStream;

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
    protected String renderSection(String padding, String background) {
        var childSections = renderChildSections();
        if (childSections.isBlank()) {
            return "";
        }

        var attrs = new LinkedHashMap<String, String>();
        attrs.put("padding", padding);
        attrs.put("background-color", hasText(backgroundColor) ? backgroundColor : background);
        if (borderRadius != null && borderRadius > 0) {
            attrs.put("border-radius", borderRadius + "px");
        }
        if (borderWidth != null && borderWidth > 0) {
            var style = borderStyle == null ? BorderStyle.SOLID : borderStyle;
            var color = hasText(borderColor) ? borderColor : EmailTheme.BORDER_COLOR;
            attrs.put("border", "%dpx %s %s".formatted(borderWidth, style.cssValue(), color));
        }

        return """
                <mj-wrapper %s>
                %s
                </mj-wrapper>""".formatted(attributes(attrs), childSections);
    }

    /** Used inside a layout column, where only column-level elements are allowed. */
    @Override
    protected String renderContent() {
        var gapPixels = gapPixels();
        var separator = gapPixels > 0 ? "\n<mj-spacer height=\"%dpx\" />\n".formatted(gapPixels) : "\n";
        return nonNullChildren().stream()
                .map(BaseEmailBlock::renderContent)
                .filter(html -> html != null && !html.isBlank())
                .collect(Collectors.joining(separator));
    }

    /** Children as bare sections: the wrapper supplies the inset, so they add only the gap. */
    private String renderChildSections() {
        var blocks = nonNullChildren();
        var gapPixels = gapPixels();
        return IntStream.range(0, blocks.size())
                .mapToObj(i -> {
                    var block = blocks.get(i);
                    var childPadding = i == 0 ? "0" : "%dpx 0 0 0".formatted(gapPixels);
                    // mj-wrapper can't nest, so a nested vertical layout contributes its children directly.
                    return block instanceof VerticalLayoutBlock nested
                            ? nested.renderChildSections()
                            : block.renderSection(childPadding, "transparent");
                })
                .filter(html -> html != null && !html.isBlank())
                .collect(Collectors.joining("\n"));
    }

    private int gapPixels() {
        return (gap == null ? Gap.NONE : gap).pixels();
    }

    private List<BaseEmailBlock> nonNullChildren() {
        return children == null
                ? List.of()
                : children.stream().filter(Objects::nonNull).toList();
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
