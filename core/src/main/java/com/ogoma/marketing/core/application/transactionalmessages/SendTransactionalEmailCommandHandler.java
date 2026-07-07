package com.ogoma.marketing.core.application.transactionalmessages;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import com.ogoma.marketing.core.implementations.EmailMessage;

public class SendTransactionalEmailCommandHandler implements CommandHandler<SendTransactionalEmailCommand, Void> {
    private final EmailTemplateRepository emailTemplateRepository;
    private final TemplateRenderer templateRenderer;
    private final MessageRouter messageRouter;

    public SendTransactionalEmailCommandHandler(EmailTemplateRepository emailTemplateRepository, TemplateRenderer templateRenderer, MessageRouter messageRouter) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.templateRenderer = templateRenderer;
        this.messageRouter = messageRouter;
    }

    @Override
    public Class<SendTransactionalEmailCommand> supports() {
        return SendTransactionalEmailCommand.class;
    }

    @Override
    public Void handle(SendTransactionalEmailCommand command) {
        EmailTemplateEntity emailTemplateEntity = this.emailTemplateRepository.getTemplateByID(command.templateID()).orElseThrow(() -> new RecordNotFoundException("Email template not found"));
        var emailTemplate = emailTemplateEntity.getEmailTemplate();
        String content = emailTemplate.renderHtml();
        String renderedTemplate = this.templateRenderer.render(content, command.params());
        EmailMessage emailMessage = new EmailMessage(
                emailTemplate.getEmailSetting().getSubject(),
                renderedTemplate,
                command.recipients()
        );
        this.messageRouter.route(emailMessage);
        return null;
    }
}
