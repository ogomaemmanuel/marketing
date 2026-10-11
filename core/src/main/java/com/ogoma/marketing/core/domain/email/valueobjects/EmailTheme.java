package com.ogoma.marketing.core.domain.email.valueobjects;

/**
 * Shared design tokens so every email block renders with a single, consistent
 * (Mailchimp-inspired) look: light grey canvas, white content cards, warm dark
 * text, and a signature yellow accent for primary calls to action.
 */
final class EmailTheme {

    private EmailTheme() {
    }

    static final String FONT_FAMILY = "'Helvetica Neue', Helvetica, Arial, sans-serif";

    // Canvas
    static final String BODY_BACKGROUND = "#f2f2f2";
    static final String CONTENT_BACKGROUND = "#ffffff";

    // Typography
    static final String TEXT_COLOR = "#241c15";
    static final String HEADING_COLOR = "#241c15";
    static final String MUTED_TEXT_COLOR = "#65605b";
    static final String LINK_COLOR = "#007c89";

    // Brand accents
    static final String ACCENT_COLOR = "#ffe01b";
    static final String ACCENT_TEXT_COLOR = "#241c15";
    static final String DARK_COLOR = "#241c15";
    static final String DARK_TEXT_COLOR = "#ffffff";
    static final String DESTRUCTIVE_COLOR = "#d1453b";
    static final String DESTRUCTIVE_TEXT_COLOR = "#ffffff";

    // Structure
    static final String BORDER_COLOR = "#e6e6e6";
    static final String BORDER_RADIUS = "4px";
    static final String CARD_BORDER_RADIUS = "8px";
}
