package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.infrastructure.workflows.abstractions.RenderTemplateActivity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public record RenderTemplateActivityImpl(
        TemplateRenderer templateRenderer
) implements RenderTemplateActivity {

    @Override
    public String render(String template, Map<String, Object> params) {
        return templateRenderer.render(template,params);
    }
}
