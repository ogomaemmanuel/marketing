package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.application.sms.commands.CreateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.commands.DuplicateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.commands.UpdateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplateByIDQueryHandler;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesQueryHandler;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SmsTemplateConfig {
    @Bean
    GetSmsTemplatesQueryHandler getSmsTemplatesQueryHandler(SmsTemplateRepository smsTemplateRepository) {
        return new GetSmsTemplatesQueryHandler(smsTemplateRepository);
    }

    @Bean
    DuplicateSmsTemplateCommandHandler duplicateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new DuplicateSmsTemplateCommandHandler(smsTemplateRepository);
    }
    @Bean
    UpdateSmsTemplateCommandHandler updateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new UpdateSmsTemplateCommandHandler(smsTemplateRepository);
    }

    @Bean
    GetSmsTemplateByIDQueryHandler getSmsTemplateByIDQueryHandler(SmsTemplateRepository smsTemplateRepository) {
        return new GetSmsTemplateByIDQueryHandler(smsTemplateRepository);
    }
    @Bean
    CreateSmsTemplateCommandHandler createSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new CreateSmsTemplateCommandHandler(smsTemplateRepository);
    }
}
