package com.ogoma.marketing.infrastructure.workflows.abstractions;

import io.temporal.activity.ActivityInterface;

import java.util.Map;
@ActivityInterface
public interface RenderTemplateActivity extends WorkflowActivity {

     String render(String template, Map<String,Object> params);
}
