package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.NotificationWorkflowStarterService;
import com.ogoma.marketing.core.application.transactionalmessages.SendSmsTransactionalCommandHandler;
import com.ogoma.marketing.core.application.transactionalmessages.SendTransactionalEmailCommandHandler;
import com.ogoma.marketing.core.application.users.SyncUserInfoCommandHandler;
import com.ogoma.marketing.core.domain.users.UsersRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "com.ogoma.marketing")
@EntityScan(basePackages = "com.ogoma.marketing")
@ComponentScan(basePackages = "com.ogoma.marketing")
public class CompositionRoot {

    @Bean
    SendSmsTransactionalCommandHandler sendSmsTransactionalCommandHandler(NotificationWorkflowStarterService notificationWorkflowStarterService) {
        return new SendSmsTransactionalCommandHandler(notificationWorkflowStarterService);
    }

    @Bean
    SendTransactionalEmailCommandHandler sendTransactionalEmailCommandHandler(
            NotificationWorkflowStarterService notificationWorkflowStarterService
    ) {
        return new SendTransactionalEmailCommandHandler(notificationWorkflowStarterService);
    }

    @Bean
    SyncUserInfoCommandHandler syncUserInfoCommandHandler(UsersRepository usersRepository) {
        return new SyncUserInfoCommandHandler(usersRepository);
    }


}
