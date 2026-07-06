package com.ogoma.marketing.infrastructure.communication;

import com.ogoma.marketing.core.abstractions.MessageSenderService;
import com.ogoma.marketing.core.implementations.EmailMessage;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailMessageSender implements MessageSenderService<EmailMessage> {
    private final JavaMailSender javaMailSender;

    public EmailMessageSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Class<EmailMessage> supports() {
        return EmailMessage.class;
    }

    @Override
    public void send(EmailMessage message) {
        try {
            MimeMessage mimeMailMessage = this.javaMailSender.createMimeMessage();
            mimeMailMessage.setRecipients(Message.RecipientType.TO, String.join(",", message.recipients()));
            mimeMailMessage.setSubject(message.subject());
            mimeMailMessage.setContent(message.content(), "text/html");
            this.javaMailSender.send(mimeMailMessage);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
