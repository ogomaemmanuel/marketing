package com.ogoma.marketing.campaignsbatchprocessor.config;

import com.ogoma.marketing.core.abstractions.MessageSenderService;
import com.ogoma.marketing.core.implementations.EmailMessage;
import com.ogoma.marketing.infrastructure.communication.EmailMessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MessageSendersConfig {
    @Bean
    MessageSenderService<EmailMessage> messageSenderService(JavaMailSender javaMailSender){
        return new EmailMessageSender(javaMailSender);
    }
}
