package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.*;
import com.ogoma.marketing.core.implementations.CommandDispatcherImpl;
import com.ogoma.marketing.core.implementations.MessageRouterImpl;
import com.ogoma.marketing.core.implementations.QueryDispatcherImpl;
import com.ogoma.marketing.infrastructure.templaterendering.PeppleTemplateRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.List;


@Configuration
public class InfraConfig {
    @Bean
    CommandDispatcher commandDispatcher(List<CommandHandler<?, ?>> handlers) {
        return new CommandDispatcherImpl(handlers);
    }

    @Bean
    QueryDispatcher queryDispatcher(List<QueryHandler<?, ?>> handlers) {
        return new QueryDispatcherImpl(handlers);
    }

    @Bean
    MessageRouter messageRouter(List<MessageSenderService<? extends Message>> messageSenderServices) {
        return new MessageRouterImpl(messageSenderServices);
    }

    @Bean
    TemplateRenderer templateRenderer() {
        return new PeppleTemplateRenderer();
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
