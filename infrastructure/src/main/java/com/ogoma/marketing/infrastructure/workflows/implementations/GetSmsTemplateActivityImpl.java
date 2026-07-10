package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import com.ogoma.marketing.infrastructure.workflows.abstractions.GetSmsTemplateActivity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public record GetSmsTemplateActivityImpl(SmsTemplateRepository smsTemplateRepository)
        implements GetSmsTemplateActivity {
    @Override
    public Optional<SmsTemplateEntity> getTemplate(SmsTemplateID id) {
        return  smsTemplateRepository.findSmsTemplateByID(id);
    }
}
