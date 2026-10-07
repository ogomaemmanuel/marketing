package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
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
public class HorizontalLayoutBlock extends BaseEmailBlock {
    private Integer columns;
    private Integer borderRadius;

    /** Either a single width applied to every column ("50" / "50%") or a comma-separated list ("30,70"). */
    private String columnWidthInPercentage;
    private List<BaseEmailBlock> children;
    private String backgroundColor;
    private Gap gap;
    private VerticalAlign verticalAlign;

    @Override
    protected String renderSection(String padding, String background) {
        var blocks = nonNullChildren();
        if (blocks.isEmpty()) {
            return "";
        }

        var widths = resolveWidths(blocks.size());
        var halfGap = resolveGap().pixels() / 2;
        var lastIndex = blocks.size() - 1;

        var columnsMjml = IntStream.range(0, blocks.size())
                .mapToObj(i -> {
                    var attrs = new LinkedHashMap<String, String>();
                    attrs.put("width", widths.get(i) + "%");
                    attrs.put("vertical-align", resolveVerticalAlign().mjmlValue());
                    var left = i == 0 ? 0 : halfGap;
                    var right = i == lastIndex ? 0 : halfGap;
                    attrs.put("padding", "0 %dpx 0 %dpx".formatted(right, left));
                    return "<mj-column%s>%s</mj-column>"
                            .formatted(attributes(attrs), blocks.get(i).renderContent());
                })
                .collect(Collectors.joining("\n"));

        var sectionAttrs = new LinkedHashMap<String, String>();
        sectionAttrs.put("padding", padding);
        sectionAttrs.put("background-color", hasText(backgroundColor) ? backgroundColor : background);
        if (borderRadius != null && borderRadius > 0) {
            sectionAttrs.put("border-radius", borderRadius + "px");
        }

        return """
                <mj-section %s>
                %s
                </mj-section>""".formatted(attributes(sectionAttrs), columnsMjml);
    }

    /** MJML can't nest columns, so inside another column the children are stacked instead. */
    @Override
    protected String renderContent() {
        var gapPixels = resolveGap().pixels();
        var separator = gapPixels > 0 ? "\n<mj-spacer height=\"%dpx\" />\n".formatted(gapPixels) : "\n";
        return nonNullChildren().stream()
                .map(BaseEmailBlock::renderContent)
                .filter(html -> html != null && !html.isBlank())
                .collect(Collectors.joining(separator));
    }

    private List<BaseEmailBlock> nonNullChildren() {
        return children == null
                ? List.of()
                : children.stream().filter(Objects::nonNull).toList();
    }

    private List<Integer> resolveWidths(int count) {
        var defaultWidth = 100 / count;
        var parsed = new ArrayList<Integer>();

        if (hasText(columnWidthInPercentage)) {
            for (var part : columnWidthInPercentage.split(",")) {
                var cleaned = part.replace("%", "").strip();
                try {
                    parsed.add(Integer.parseInt(cleaned));
                } catch (NumberFormatException e) {
                    parsed.clear();
                    break;
                }
            }
        }

        return IntStream.range(0, count)
                .mapToObj(i -> switch (parsed.size()) {
                    case 0 -> defaultWidth;
                    case 1 -> parsed.getFirst();
                    default -> i < parsed.size() ? parsed.get(i) : defaultWidth;
                })
                .toList();
    }

    private Gap resolveGap() {
        return gap == null ? Gap.NONE : gap;
    }

    private VerticalAlign resolveVerticalAlign() {
        return verticalAlign == null ? VerticalAlign.TOP : verticalAlign;
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
    public enum VerticalAlign {
        TOP("top"), CENTER("middle"), BOTTOM("bottom");

        private final String mjmlValue;

        VerticalAlign(String mjmlValue) {
            this.mjmlValue = mjmlValue;
        }

        String mjmlValue() {
            return mjmlValue;
        }

        @JsonCreator
        public static VerticalAlign fromString(String value){
            return VerticalAlign.valueOf(value.toUpperCase());
        }
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
        public static Gap fromString(String value){
            return Gap.valueOf(value.toUpperCase());
        }
    }
}