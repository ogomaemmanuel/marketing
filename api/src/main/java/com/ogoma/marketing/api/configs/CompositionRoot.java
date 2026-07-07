package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.*;
import com.ogoma.marketing.core.application.email.commands.CloneEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.commands.CreateEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDQueryHandler;
import com.ogoma.marketing.core.application.sms.CreateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.DuplicateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesQueryHandler;
import com.ogoma.marketing.core.application.transactionalmessages.SendSmsTransactionalCommandHandler;
import com.ogoma.marketing.core.application.transactionalmessages.SendTransactionalEmailCommandHandler;
import com.ogoma.marketing.core.application.users.SyncUserInfoCommandHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import com.ogoma.marketing.core.domain.users.UsersRepository;
import com.ogoma.marketing.core.implementations.CommandDispatcherImpl;
import com.ogoma.marketing.core.implementations.MessageRouterImpl;
import com.ogoma.marketing.core.implementations.QueryDispatcherImpl;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;

@Configuration
@EnableJdbcRepositories(basePackages = "com.ogoma.marketing")
@EntityScan(basePackages = "com.ogoma.marketing")
@ComponentScan(basePackages = "com.ogoma.marketing")
public class CompositionRoot {

    @Bean
    CommandDispatcher commandDispatcher(List<CommandHandler<?, ?>> handlers) {
        return new CommandDispatcherImpl(handlers);
    }

    @Bean
    QueryDispatcher queryDispatcher(List<QueryHandler<?, ?>> handlers) {
        return new QueryDispatcherImpl(handlers);
    }

    @Bean
    CreateEmailTemplateCommandHandler createEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new CreateEmailTemplateCommandHandler(emailTemplateRepository);
    }

    @Bean
    GetEmailTemplateByIDQueryHandler getEmailTemplateByIDQueryHandler(EmailTemplateRepository emailTemplateRepository) {
        return new GetEmailTemplateByIDQueryHandler(emailTemplateRepository);
    }

    @Bean
    CloneEmailTemplateCommandHandler cloneEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new CloneEmailTemplateCommandHandler(emailTemplateRepository);
    }

    @Bean
    CreateSmsTemplateCommandHandler createSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new CreateSmsTemplateCommandHandler(smsTemplateRepository);
    }

    @Bean
    GetSmsTemplatesQueryHandler getSmsTemplatesQueryHandler(SmsTemplateRepository smsTemplateRepository) {
        return new GetSmsTemplatesQueryHandler(smsTemplateRepository);
    }

    @Bean
    DuplicateSmsTemplateCommandHandler duplicateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new DuplicateSmsTemplateCommandHandler(smsTemplateRepository);
    }

    @Bean
    MessageRouter messageRouter(List<MessageSenderService<? extends Message>> messageSenderServices) {
        return new MessageRouterImpl(messageSenderServices);
    }

    @Bean
    SendSmsTransactionalCommandHandler sendSmsTransactionalCommandHandler(
            MessageRouter messageRouter, SmsTemplateRepository smsTemplateRepository, TemplateRenderer templateRenderer) {
        return new SendSmsTransactionalCommandHandler(smsTemplateRepository, messageRouter, templateRenderer);
    }

    @Bean
    SendTransactionalEmailCommandHandler sendTransactionalEmailCommandHandler(
            MessageRouter messageRouter, EmailTemplateRepository emailTemplateRepository, TemplateRenderer templateRenderer
    ) {
        return new SendTransactionalEmailCommandHandler(emailTemplateRepository, templateRenderer, messageRouter);
    }

    @Bean
    SyncUserInfoCommandHandler syncUserInfoCommandHandler(UsersRepository usersRepository) {
        return new SyncUserInfoCommandHandler(usersRepository);
    }

    @Bean
    TemplateRenderer templateRenderer() {
        return new TemplateRenderer();
    }

}
