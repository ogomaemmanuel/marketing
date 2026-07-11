package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.application.transactionalmessages.SendSmsTransactionalCommand;
import com.ogoma.marketing.core.application.transactionalmessages.SendTransactionalEmailCommand;
import com.ogoma.marketing.core.application.transactionalmessages.TransactionalNotification;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailSetting;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import com.ogoma.marketing.core.implementations.EmailMessage;
import com.ogoma.marketing.core.implementations.SmsMessage;
import com.ogoma.marketing.infrastructure.workflows.abstractions.*;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.Optional;

public class MessageSenderWorkflowImpl implements MessageSenderWorkflow {

    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(60))
            .build();
    private final GetEmailTemplateActivity activity = Workflow.newActivityStub(GetEmailTemplateActivity.class, options);
    private final GetSmsTemplateActivity getSmsTemplateActivity = Workflow.newActivityStub(GetSmsTemplateActivity.class, options);
    private final RenderTemplateActivity renderTemplateActivity = Workflow.newActivityStub(RenderTemplateActivity.class, options);
    private final SendMessageActivity sendMessageActivity = Workflow.newActivityStub(SendMessageActivity.class, options);

    @Override
    public void startWorkflow(TransactionalNotification transactionalNotification) {
        switch (transactionalNotification) {
            case SendSmsTransactionalCommand command ->
                    this.getSmsTemplateActivity.getTemplate(command.templateID()).ifPresent((smsTemplateEntity) -> {
                        String smsContent = renderTemplateActivity.render(smsTemplateEntity.getContent(), command.params());
                        SmsMessage smsMessage = new SmsMessage(command.recipient(), smsContent);
                        sendMessageActivity.send(smsMessage);
                    });
            case SendTransactionalEmailCommand command ->
                    activity.getEmailTemplate(command.templateID()).ifPresent(emailTemplateEntity -> {
                        String emailContent = renderTemplateActivity.render(emailTemplateEntity.getEmailTemplate().renderHtml(), command.params());
                        EmailMessage emailMessage = new EmailMessage(
                                Optional.ofNullable(emailTemplateEntity.getEmailTemplate()).map(EmailTemplate::getEmailSetting).map(EmailSetting::getSubject).orElse(""),
                                emailContent,
                                command.recipients());
                        sendMessageActivity.send(emailMessage);
                    });

        }
    }
}
