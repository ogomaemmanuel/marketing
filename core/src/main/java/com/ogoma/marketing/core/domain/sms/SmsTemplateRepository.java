package com.ogoma.marketing.core.domain.sms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SmsTemplateRepository {

    SmsTemplateEntity saveSmsTemplate(SmsTemplateEntity smsTemplateEntity);
    Optional<SmsTemplateEntity> findSmsTemplateByID(SmsTemplateID smsTemplateID);
    Page<SmsTemplateEntity> findSmsTemplates(Pageable pageable);
    Page<SmsTemplateEntity> findSmsTemplates(String searchTerm,Pageable pageable);

}
