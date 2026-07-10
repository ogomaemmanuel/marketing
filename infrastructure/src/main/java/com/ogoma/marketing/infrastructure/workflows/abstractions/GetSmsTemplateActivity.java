package com.ogoma.marketing.infrastructure.workflows.abstractions;

import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import io.temporal.activity.ActivityInterface;

import java.util.Optional;
@ActivityInterface
public interface GetSmsTemplateActivity extends WorkflowActivity {
    Optional<SmsTemplateEntity> getTemplate(SmsTemplateID id);
}
