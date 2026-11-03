package com.ogoma.marketing.campaignsbatchprocessor.config;

import com.ogoma.marketing.core.abstractions.MessageSenderService;
import com.ogoma.marketing.core.implementations.EmailMessage;
import com.ogoma.marketing.core.implementations.SmsMessage;
import com.ogoma.marketing.infrastructure.communication.EmailMessageSender;
import com.ogoma.marketing.infrastructure.communication.SmsMessageSender;
import com.ogoma.marketing.infrastructure.composition.EngagementTrackingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@Import(
        EngagementTrackingConfig.class
)
public class MessageSendersConfig {
    @Bean
    MessageSenderService<EmailMessage> messageSenderService(JavaMailSender javaMailSender) {
        return new EmailMessageSender(javaMailSender);
    }

    @Bean
    MessageSenderService<SmsMessage> smsMessageMessageSenderService() {
        return new SmsMessageSender();
    }
}
