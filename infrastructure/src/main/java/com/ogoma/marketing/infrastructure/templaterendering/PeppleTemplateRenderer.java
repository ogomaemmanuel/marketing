package com.ogoma.marketing.infrastructure.templaterendering;

import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.StringLoader;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.io.StringWriter;
import java.util.Map;

public class PeppleTemplateRenderer implements TemplateRenderer {
    private final PebbleEngine pebbleEngine;

    public PeppleTemplateRenderer() {
        // Initialize once as a singleton/bean
        this.pebbleEngine = new PebbleEngine.Builder()
                .loader(new StringLoader())
                .build();
    }

    @Override
    public String render(String templateContent, Map<String, Object> params) {
        try {
            PebbleTemplate template = pebbleEngine.getTemplate(templateContent);
            StringWriter writer = new StringWriter();
            template.evaluate(writer, params);
            return writer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to render template", ex);
        }
    }
}
