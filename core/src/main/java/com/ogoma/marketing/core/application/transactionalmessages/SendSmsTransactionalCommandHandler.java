package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import com.ogoma.marketing.core.implementations.SmsMessage;

public class SendSmsTransactionalCommandHandler implements CommandHandler<SendSmsTransactionalCommand, Void> {
    private final SmsTemplateRepository smsTemplateRepository;
    private final MessageRouter messageRouter;
    private final TemplateRenderer templateRenderer;

    public SendSmsTransactionalCommandHandler(SmsTemplateRepository smsTemplateRepository, MessageRouter messageRouter, TemplateRenderer templateRenderer) {
        this.smsTemplateRepository = smsTemplateRepository;
        this.messageRouter = messageRouter;
        this.templateRenderer = templateRenderer;
    }

    @Override
    public Class<SendSmsTransactionalCommand> supports() {
        return SendSmsTransactionalCommand.class;
    }

    @Override
    public Void handle(SendSmsTransactionalCommand command) {
        SmsTemplateEntity smsTemplateEntity =
                this.smsTemplateRepository.findSmsTemplateByID(command.templateID()).orElseThrow(() -> new RecordNotFoundException(String.format("Sms template with id %s not found", command.templateID())));
        String content = templateRenderer.render(
                smsTemplateEntity.getContent(),
                command.params());
        messageRouter.route(
                new SmsMessage(
                        command.recipient(),
                        content));
        return null;

    }
}
