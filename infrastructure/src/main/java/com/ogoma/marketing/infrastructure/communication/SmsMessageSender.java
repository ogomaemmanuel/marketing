package com.ogoma.marketing.infrastructure.communication;

import com.ogoma.marketing.core.abstractions.MessageSenderService;
import com.ogoma.marketing.core.implementations.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SmsMessageSender implements MessageSenderService<SmsMessage> {
    private static final Logger log = LoggerFactory.getLogger(SmsMessageSender.class);

    @Override
    public Class<SmsMessage> supports() {
        return SmsMessage.class;
    }

    @Override
    public void send(SmsMessage message) {
        log.info("Sending sms for {}", message);
    }
}
