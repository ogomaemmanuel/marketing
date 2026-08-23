package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.application.contacts.commands.AddContactCommandHandler;
import com.ogoma.marketing.core.application.contacts.commands.AudienceMembershipValidator;
import com.ogoma.marketing.core.application.contacts.commands.UpdateContactCommandHandler;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDQueryHandler;
import com.ogoma.marketing.core.application.contacts.queries.GetContactsQueryHandler;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactsConfig {
    @Bean
    AddContactCommandHandler addContactCommandHandler(
            ContactRepository contactRepository,
            AudienceRepository audienceRepository,
            AudienceMembershipRepository audienceMembershipRepository,
            UnitOfWork unitOfWork
    ) {
        return new AddContactCommandHandler(contactRepository, audienceRepository, audienceMembershipRepository, unitOfWork);
    }
    @Bean
    UpdateContactCommandHandler updateContactCommandHandler(
            ContactRepository contactRepository,
            AudienceMembershipValidator audienceMembershipValidator,
            AudienceMembershipRepository audienceMembershipRepository,
            UnitOfWork unitOfWork
    ) {
        return new UpdateContactCommandHandler(
                contactRepository,
                audienceMembershipValidator,
                audienceMembershipRepository, unitOfWork);
    }
    @Bean
    GetContactByIDQueryHandler getContactByIDQueryHandler(ContactRepository contactRepository) {
        return new GetContactByIDQueryHandler(contactRepository);
    }
    @Bean
    GetContactsQueryHandler getContactsQueryHandler(ContactRepository contactRepository) {
        return new GetContactsQueryHandler(contactRepository);
    }


}
