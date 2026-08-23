package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.application.audience.commands.AddAudienceCommandHandler;
import com.ogoma.marketing.core.application.audience.commands.UpdateAudienceCommandHandler;
import com.ogoma.marketing.core.application.audience.queries.GetAudienceByIDQueryHandler;
import com.ogoma.marketing.core.application.audience.queries.GetAudiencesQueryHandler;
import com.ogoma.marketing.core.application.contacts.commands.AudienceMembershipValidator;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudienceConfig {
    @Bean
    AddAudienceCommandHandler addAudienceCommandHandler(
            AudienceRepository audienceRepository
    ) {
        return new AddAudienceCommandHandler(audienceRepository);
    }

    @Bean
    UpdateAudienceCommandHandler updateAudienceCommandHandler(AudienceRepository audienceRepository) {
        return new UpdateAudienceCommandHandler(audienceRepository);
    }

    @Bean
    GetAudienceByIDQueryHandler getAudienceByIDQueryHandler(AudienceRepository audienceRepository) {
        return new GetAudienceByIDQueryHandler(audienceRepository);
    }

    @Bean
    GetAudiencesQueryHandler getAudiencesQueryHandler(AudienceRepository audienceRepository) {
        return new GetAudiencesQueryHandler(audienceRepository);
    }
    @Bean
    AudienceMembershipValidator audienceMembershipValidator(AudienceRepository audienceRepository) {
        return new AudienceMembershipValidator(audienceRepository);
    }
}
