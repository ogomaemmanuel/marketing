package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class HeroBlock extends BaseEmailBlock {

    private String title;
    private String subtitle;
    private String backgroundColor;
    private String imageUrl;
    private String ctaButtonText;
    private String ctaLink;
    private ImagePosition imagePosition;
    private ButtonVariant buttonVariant;
    private ButtonSize buttonSize;
    private String minHeight;

    public enum ImagePosition {
        LEFT,
        RIGHT,
        TOP,
        BACKGROUND;

        @JsonCreator
        public static ImagePosition fromString(String value) {
            return ImagePosition.valueOf(value.toUpperCase());
        }
    }

    public enum ButtonVariant {
        PRIMARY,
        SECONDARY,
        OUTLINE;

        @JsonCreator
        public static ButtonVariant fromString(String value) {
            return ButtonVariant.valueOf(value.toUpperCase());
        }
    }

    public enum ButtonSize {
        SMALL,
        MEDIUM,
        LARGE;

        @JsonCreator
        public static ButtonSize fromString(String value) {
            return ButtonSize.valueOf(value.toUpperCase());
        }
    }

    @Override
    public String renderHtml() {
        var position = imagePosition == null
                ? ImagePosition.LEFT
                : imagePosition;

        return switch (position) {
            case LEFT -> renderHorizontalHero(false);
            case RIGHT -> renderHorizontalHero(true);
            case TOP -> renderTopHero();
            case BACKGROUND -> renderBackgroundHero();
        };
    }

    /**
     * Hero with the image used as a background.
     *
     * Layout:
     *
     *              background image
     *          +----------------------+
     *          |        title         |
     *          |       subtitle       |
     *          |        button        |
     *          +----------------------+
     */
    private String renderBackgroundHero() {
        return """
                <mj-section
                    background-color="%s"
                    background-url="%s"
                    background-size="cover"
                    background-position="center center"
                    background-repeat="no-repeat"
                    height="%s"
                    border-radius="%s"
                    padding="0">

                    <mj-column
                        width="100%%"
                        vertical-align="middle"
                        padding="32px">

                        <mj-text
                            align="center"
                            color="%s"
                            font-size="48px"
                            font-weight="700"
                            line-height="1.1"
                            padding="0 0 16px 0">
                            %s
                        </mj-text>

                        <mj-text
                            align="center"
                            color="%s"
                            font-size="24px"
                            line-height="1.4"
                            padding="0 0 24px 0">
                            %s
                        </mj-text>

                        %s

                    </mj-column>

                </mj-section>
                """.formatted(
                colorOrDefault(backgroundColor, EmailTheme.DARK_COLOR),
                escapeAttribute(imageUrl),
                valueOrDefault(minHeight, "400px"),
                EmailTheme.CARD_BORDER_RADIUS,
                EmailTheme.DARK_TEXT_COLOR,
                escapeHtml(title),
                EmailTheme.DARK_TEXT_COLOR,
                escapeHtml(subtitle),
                renderButton("center")
        );
    }

    /**
     * Horizontal hero.
     *
     * LEFT:
     *   text | image
     *
     * RIGHT:
     *   image | text
     */
    private String renderHorizontalHero(boolean imageFirst) {

        var textColumn = """
                <mj-column
                    width="50%%"
                    vertical-align="middle"
                    padding="32px">

                    <mj-text
                        color="%s"
                        font-size="48px"
                        font-weight="700"
                        line-height="1.1"
                        padding="0 0 16px 0">
                        %s
                    </mj-text>

                    <mj-text
                        color="%s"
                        font-size="24px"
                        line-height="1.4"
                        padding="0 0 24px 0">
                        %s
                    </mj-text>

                    %s

                </mj-column>
                """.formatted(
                EmailTheme.DARK_TEXT_COLOR,
                escapeHtml(title),
                EmailTheme.DARK_TEXT_COLOR,
                escapeHtml(subtitle),
                renderButton("left")
        );

        var imageColumn = """
                <mj-column
                    width="50%%"
                    vertical-align="middle"
                    padding="32px">

                    <mj-image
                        src="%s"
                        alt="Hero"
                        width="100%%"
                        border-radius="%s"
                        padding="0"
                        fluid-on-mobile="true"/>

                </mj-column>
                """.formatted(
                escapeAttribute(imageUrl),
                EmailTheme.BORDER_RADIUS
        );

        return """
                <mj-section
                    background-color="%s"
                    padding="0"
                    border-radius="%s">

                    <mj-group>
                        %s
                        %s
                    </mj-group>

                </mj-section>
                """.formatted(
                colorOrDefault(backgroundColor, EmailTheme.DARK_COLOR),
                EmailTheme.CARD_BORDER_RADIUS,
                imageFirst ? imageColumn : textColumn,
                imageFirst ? textColumn : imageColumn
        );
    }

    /**
     * Top hero.
     *
     * Layout:
     *
     *   +------------------------+
     *   |         image          |
     *   +------------------------+
     *   |         title          |
     *   |        subtitle        |
     *   |         button         |
     *   +------------------------+
     *
     * The image intentionally uses its natural height.
     *
     * Do not use:
     *
     *     height="300px"
     *     object-fit="cover"
     *
     * here because these can produce inconsistent rendering and
     * overflow in email clients, particularly when combined with
     * fluid-on-mobile.
     */
    private String renderTopHero() {
        return """
            <mj-section
                background-color="%s"
                padding="0"
                border-radius="%s">
                <mj-column
                    width="100%%"
                    vertical-align="top">

                    <mj-image
                        src="%s"
                        alt="Hero"
                        height="%s"
                        width="100%%"
                        padding="0"
                        border-radius="%s %s 0 0"
                        fluid-on-mobile="true"/>

                    <mj-text
                        align="center"
                        color="%s"
                        font-size="48px"
                        font-weight="700"
                        line-height="1.1"
                        padding="32px 32px 16px 32px">
                        %s
                    </mj-text>

                    <mj-text
                        align="center"
                        color="%s"
                        font-size="24px"
                        line-height="1.4"
                        padding="0 32px 24px 32px">
                        %s
                    </mj-text>
                    %s
                </mj-column>

            </mj-section>
            """.formatted(
                colorOrDefault(backgroundColor, EmailTheme.DARK_COLOR),
                EmailTheme.CARD_BORDER_RADIUS,
                escapeAttribute(imageUrl),
                valueOrDefault( minHeight,"400px"),
                EmailTheme.CARD_BORDER_RADIUS,
                EmailTheme.CARD_BORDER_RADIUS,
                EmailTheme.DARK_TEXT_COLOR,
                escapeHtml(title),
                EmailTheme.DARK_TEXT_COLOR,
                escapeHtml(subtitle),
                renderButton("center", "0 0 32px 0")
        );
    }

    /**
     * Render CTA button.
     */
    private String renderButton(String align) {
        return renderButton(align, "0");
    }

    private String renderButton(String align, String padding) {

        if (StringUtils.isBlank(ctaButtonText)) {
            return "";
        }

        var resolvedButtonSize = buttonSize == null
                ? ButtonSize.MEDIUM
                : buttonSize;

        var size = switch (resolvedButtonSize) {
            case SMALL -> new ButtonStyle(
                    "14px",
                    "4px 12px"
            );

            case MEDIUM -> new ButtonStyle(
                    "16px",
                    "8px 16px"
            );

            case LARGE -> new ButtonStyle(
                    "18px",
                    "12px 24px"
            );
        };

        var resolvedButtonVariant = buttonVariant == null
                ? ButtonVariant.PRIMARY
                : buttonVariant;

        return switch (resolvedButtonVariant) {

            case PRIMARY -> """
                <mj-button
                    href="%s"
                    background-color="%s"
                    color="%s"
                    font-size="%s"
                    font-weight="500"
                    border-radius="%s"
                    inner-padding="%s"
                    padding="%s"
                    align="%s">
                    %s
                </mj-button>
                """.formatted(
                    escapeAttribute(ctaLink),
                    EmailTheme.ACCENT_COLOR,
                    EmailTheme.ACCENT_TEXT_COLOR,
                    size.fontSize(),
                    EmailTheme.BORDER_RADIUS,
                    size.padding(),
                    padding,
                    align,
                    escapeHtml(ctaButtonText)
            );

            case SECONDARY -> """
                <mj-button
                    href="%s"
                    background-color="%s"
                    color="%s"
                    font-size="%s"
                    font-weight="500"
                    border-radius="%s"
                    inner-padding="%s"
                    padding="%s"
                    align="%s">
                    %s
                </mj-button>
                """.formatted(
                    escapeAttribute(ctaLink),
                    EmailTheme.CONTENT_BACKGROUND,
                    EmailTheme.DARK_COLOR,
                    size.fontSize(),
                    EmailTheme.BORDER_RADIUS,
                    size.padding(),
                    padding,
                    align,
                    escapeHtml(ctaButtonText)
            );

            case OUTLINE -> """
                <mj-button
                    href="%s"
                    background-color="transparent"
                    color="%s"
                    font-size="%s"
                    font-weight="500"
                    border="2px solid %s"
                    border-radius="%s"
                    inner-padding="%s"
                    padding="%s"
                    align="%s">
                    %s
                </mj-button>
                """.formatted(
                    escapeAttribute(ctaLink),
                    EmailTheme.DARK_TEXT_COLOR,
                    size.fontSize(),
                    EmailTheme.DARK_TEXT_COLOR,
                    EmailTheme.BORDER_RADIUS,
                    size.padding(),
                    padding,
                    align,
                    escapeHtml(ctaButtonText)
            );
        };
    }

    private record ButtonStyle(
            String fontSize,
            String padding
    ) {
    }

    private static String valueOrDefault(
            String value,
            String defaultValue
    ) {
        return value == null || value.isBlank()
                ? defaultValue
                : value;
    }

    private static String colorOrDefault(
            String value,
            String defaultValue
    ) {
        return value == null || value.isBlank()
                ? defaultValue
                : value;
    }

    private static String escapeHtml(String value) {
        return StringEscapeUtils.escapeHtml4(
                StringUtils.defaultString(value)
        );
    }

    private static String escapeAttribute(String value) {
        return escapeHtml(value);
    }
}