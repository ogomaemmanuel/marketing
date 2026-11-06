package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.application.polls.CreatePollCommandHandler;
import com.ogoma.marketing.core.domain.polls.repository.PollRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PollsConfig {


    @Bean
    CreatePollCommandHandler createPollCommandHandler(
            PollRepository pollRepository,
            UnitOfWork unitOfWork,
            Clock clock
    ){
        return new CreatePollCommandHandler(pollRepository,unitOfWork,clock);
    }
}
