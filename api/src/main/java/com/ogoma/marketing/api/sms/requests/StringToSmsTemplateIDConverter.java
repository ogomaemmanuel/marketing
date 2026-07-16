package com.ogoma.marketing.api.sms.requests;

import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToSmsTemplateIDConverter implements Converter<String, SmsTemplateID> {
    @Override
    public SmsTemplateID convert(String source) {
        return new SmsTemplateID(UUID.fromString(source));
    }
}
