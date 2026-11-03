package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.jcputney.mjml.MjmlRenderResult;
import dev.jcputney.mjml.MjmlRenderer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serial;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public class EmailTemplate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("metadata")
    private EmailTemplateMetaData metaData;

    @JsonProperty("settings")
    private EmailSetting emailSetting;

    @JsonProperty("blocks")
    private List<BaseEmailBlock> blocks = new ArrayList<>();

    /**
     * Renders the email HTML and injects delivery/open tracking.
     *
     * @param trackingBaseUrl The base URL of your tracking service (e.g., "https://track.yourdomain.com")
     * @param trackingId      The unique tracking identifier for the recipient/campaign
     * @return The fully rendered and tracked HTML string
     */
    public String renderHtml(String trackingBaseUrl, String trackingId) {
        String blocksHtml = Optional.ofNullable(this.blocks)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .map(BaseEmailBlock::renderHtml)
                .collect(Collectors.joining("\n          "));

        String subject = Optional.ofNullable(emailSetting)
                .map(EmailSetting::getSubject)
                .filter(s -> !s.isBlank())
                .orElse("Notification");

        String preview = Optional.ofNullable(emailSetting)
                .map(EmailSetting::getPreviewText)
                .filter(text -> !text.isBlank())
                .map(text -> "<mj-preview>" + EmailTemplateSafety.text(text) + "</mj-preview>")
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
                EmailTemplateSafety.text(subject),
                preview,
                EmailTheme.FONT_FAMILY,
                EmailTheme.TEXT_COLOR,
                EmailTheme.FONT_FAMILY,
                EmailTheme.TEXT_COLOR,
                EmailTheme.BODY_BACKGROUND,
                blocksHtml
        );

        MjmlRenderResult result = MjmlRenderer.render(mjmlString);
        String finalHtml = result.html();

        // Inject tracking pixel and rewrite links if tracking info is available
        if (isValidTrackingInfo(trackingBaseUrl, trackingId)) {
            finalHtml = injectTracking(finalHtml, trackingBaseUrl, trackingId);
        }

        return finalHtml;
    }

    private boolean isValidTrackingInfo(String trackingBaseUrl, String trackingId) {
        return EmailTemplateSafety.trackingBase(trackingBaseUrl) != null
                && trackingId != null && !trackingId.isBlank()
                && trackingId.matches("[A-Za-z0-9_-]{1,128}");
    }

    private String injectTracking(String html, String trackingBaseUrl, String trackingId) {
        // 1. Insert Open Tracking Pixel before </body>
        String trackingPixel = String.format(
                "<img src=\"%s/api/v1/engagement-tracking/open/%s\" width=\"1\" height=\"1\" style=\"display:none;\" alt=\"\" />",
                EmailTemplateSafety.attribute(EmailTemplateSafety.trackingBase(trackingBaseUrl)), trackingId
        );

        String bodyEndTag = "</body>";
        if (html.contains(bodyEndTag)) {
            html = html.replace(bodyEndTag, trackingPixel + bodyEndTag);
        } else {
            html += trackingPixel;
        }

        // 2. Rewrite anchor links for click tracking using Jsoup
        return rewriteLinksForTracking(html, trackingBaseUrl, trackingId);
    }

    private String rewriteLinksForTracking(String html, String trackingBaseUrl, String trackingId) {
        try {
            Document doc = Jsoup.parse(html);
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("abs:href");

                // Only rewrite standard http/https web links and avoid already tracked links
                boolean isHttpOrHttps = href.startsWith("http://") || href.startsWith("https://");
                boolean isAlreadyTracked = href.contains("/api/v1/engagement-tracking/click");

                if (isHttpOrHttps && !isAlreadyTracked) {
                    String encodedHref = URLEncoder.encode(href, StandardCharsets.UTF_8);
                    String trackedUrl = String.format("%s/api/v1/engagement-tracking/click/%s?redirect=%s",
                            EmailTemplateSafety.trackingBase(trackingBaseUrl),
                            URLEncoder.encode(trackingId, StandardCharsets.UTF_8), encodedHref);
                    link.attr("href", trackedUrl);
                }
            }
            doc.outputSettings().prettyPrint(false);
            return doc.html();
        } catch (Exception e) {
            log.error("Failed to parse and rewrite HTML links for tracking ID: {}", trackingId, e);
            return html;
        }
    }

    public String renderHtml() {
        return renderHtml(null, null);
    }
}
