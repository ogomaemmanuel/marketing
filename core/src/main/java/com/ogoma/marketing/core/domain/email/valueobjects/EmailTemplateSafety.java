package com.ogoma.marketing.core.domain.email.valueobjects;

import org.apache.commons.lang3.StringEscapeUtils;

import java.net.URI;
import java.util.Locale;

/** Context-specific safeguards for values inserted into generated email markup. */
final class EmailTemplateSafety {
    private EmailTemplateSafety() {}

    static String text(String value) {
        return StringEscapeUtils.escapeHtml4(value == null ? "" : value);
    }

    static String attribute(String value) {
        return StringEscapeUtils.escapeXml10(value == null ? "" : value);
    }

    /** Permit ordinary web links and common email links; reject executable schemes. */
    static String url(String value) {
        if (value == null || value.isBlank()) return "#";
        String candidate = value.strip();
        try {
            URI uri = URI.create(candidate);
            String scheme = uri.getScheme();
            if (scheme == null) return candidate.startsWith("//") ? "#" : candidate;
            return switch (scheme.toLowerCase(Locale.ROOT)) {
                case "http", "https", "mailto", "tel" -> candidate;
                default -> "#";
            };
        } catch (IllegalArgumentException ex) {
            return "#";
        }
    }

    static String trackingBase(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            URI uri = URI.create(value.strip());
            String scheme = uri.getScheme();
            if (uri.getHost() == null || scheme == null
                    || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))
                    || uri.getRawUserInfo() != null || uri.getRawQuery() != null || uri.getRawFragment() != null) return null;
            return value.strip().replaceAll("/+$", "");
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    static String color(String value, String fallback) {
        if (value == null || !value.matches("(?i)(#[0-9a-f]{3,8}|transparent|[a-z]{1,20})")) return fallback;
        return value;
    }

    static String dimension(String value, String fallback) {
        if (value == null || !value.matches("(?i)(auto|\\d{1,4}(px|%))")) return fallback;
        return value;
    }
}
