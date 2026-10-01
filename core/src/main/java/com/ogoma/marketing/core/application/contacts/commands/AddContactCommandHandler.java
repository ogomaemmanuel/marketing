package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipEntity;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;

import java.util.List;
import java.util.Set;

public record AddContactCommandHandler(
        ContactRepository contactRepository,

        AudienceRepository audienceRepository,
        AudienceMembershipRepository audienceMembershipRepository,
        UnitOfWork unitOfWork

) implements CommandHandler<AddContactCommand, ContactEntity> {
    @Override
    public Class<AddContactCommand> supports() {
        return AddContactCommand.class;
    }

    @Override
    public ContactEntity handle(AddContactCommand command) {
        return unitOfWork.execute(() -> {
            Set<AudienceId> audienceIds = command.audienceIds() == null ? Set.of() : command.audienceIds();
            validateStaticAudiences(audienceIds);
            ContactEntity contact =
                    ContactEntity.createNew(
                            command.firstName(),
                            command.lastName(),
                            command.email(),
                            command.phoneNumber(),
                            command.attributes(),
                            command.userId()
                    );
            contactRepository.save(contact);
            if (!audienceIds.isEmpty()) {
                List<AudienceMembershipEntity> memberships =
                        audienceIds.stream()
                                .map(id ->
                                        AudienceMembershipEntity.join(
                                                contact.getId(),
                                                id))
                                .toList();
                audienceMembershipRepository.saveAll(memberships);
            }
            return contact;
        });
    }

    private void validateStaticAudiences(Set<AudienceId> audienceIds) {
        AudienceMembershipValidator audienceMembershipValidator = new AudienceMembershipValidator(audienceRepository);
        audienceMembershipValidator.validateManualMembership(audienceIds);
    }
}
