package com.ogoma.marketing.core.domain.email.valueobjects;


import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;


@Getter
@Setter
@JsonSerialize
@JsonDeserialize
public class SocialShareBlock extends BaseEmailBlock {
    private Platforms platforms;
    private Style style;
    private Size size;
    private Spacing spacing;
    private boolean showLabels;

    @Override
    public String renderHtml() {
        return null;
    }

    // --- Inner Classes ---

    @Setter
    @Getter
    public static class Platforms {
        // Getters and Setters
        private Platform facebook;
        private TwitterPlatform twitter;
        private Platform linkedin;
        private Platform instagram;
        private Platform youtube;
        private Platform tiktok;
        private Platform github;
        private EmailPlatform email;

    }

    @Setter
    @Getter
    public static class Platform {
        // Getters and Setters
        private boolean enabled;
        private String url;

    }

    @Setter
    @Getter
    public static class TwitterPlatform extends Platform {
        private String text;

    }

    @Setter
    @Getter
    public static class EmailPlatform {
        private boolean enabled;
        private String subject;
        private String body;

    }

    // --- Enums for style, size, spacing ---

    public enum Style {
        ICONS, BUTTONS, ROUNDED
    }

    public enum Size {
        SMALL, MEDIUM, LARGE
    }

    public enum Spacing {
        TIGHT, NORMAL, LOOSE
    }

    // --- Getters and Setters ---


}
