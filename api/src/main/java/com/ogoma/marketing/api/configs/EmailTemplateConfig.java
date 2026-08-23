package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.application.email.commands.CloneEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.commands.CreateEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.commands.UpdateEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDQueryHandler;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplatesQueryHandler;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailTemplateConfig {
    @Bean
    CreateEmailTemplateCommandHandler createEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new CreateEmailTemplateCommandHandler(emailTemplateRepository);
    }

    @Bean
    UpdateEmailTemplateCommandHandler updateEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new UpdateEmailTemplateCommandHandler(emailTemplateRepository);
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
    GetEmailTemplatesQueryHandler getTemplatesQueryHandler(EmailTemplateRepository emailTemplateRepository) {
        return new GetEmailTemplatesQueryHandler(emailTemplateRepository);
    }
}
