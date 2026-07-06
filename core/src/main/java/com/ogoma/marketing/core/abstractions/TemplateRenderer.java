package com.ogoma.marketing.core.abstractions;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.StringLoader;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.io.StringWriter;
import java.util.Map;

public class TemplateRenderer {
    private final PebbleEngine pebbleEngine;

    public TemplateRenderer() {
        // Initialize once as a singleton/bean
        this.pebbleEngine = new PebbleEngine.Builder()
                .loader(new StringLoader())
                .build();
    }

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
