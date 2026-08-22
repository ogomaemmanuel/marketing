package com.ogoma.marketing.core.abstractions;

import java.util.Map;

public interface TemplateRenderer {
    String render(String templateContent, Map<String, Object> params);

}
