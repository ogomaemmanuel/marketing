package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipEntity;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

import java.util.List;
import java.util.Set;

public record UpdateContactCommandHandler(
        ContactRepository contactRepository,
        AudienceMembershipValidator audienceMembershipValidator,
        AudienceMembershipRepository audienceMembershipRepository,
        UnitOfWork unitOfWork
) implements CommandHandler<UpdateContactCommand, ContactEntity> {
    @Override
    public Class<UpdateContactCommand> supports() {
        return UpdateContactCommand.class;
    }

    @Override
    public ContactEntity handle(UpdateContactCommand command) {
        return unitOfWork.execute(() -> {
            ContactEntity contactEntity = this.contactRepository.findById(command.contactID()).orElseThrow(() -> new RecordNotFoundException("Contact not found"));
            Set<AudienceId> audienceIds = command.audienceIds() == null ? Set.of() : command.audienceIds();
            validateStaticAudiences(audienceIds);
            contactEntity.update(
                    command.firstName(),
                    command.lastName(),
                    command.email(),
                    command.attributes(),
                    command.userId());
            contactRepository.save(contactEntity);
            this.audienceMembershipRepository.replaceMemberships(contactEntity.getId(), audienceIds);
            if (!audienceIds.isEmpty()) {
                List<AudienceMembershipEntity> memberships =
                        audienceIds.stream()
                                .map(id ->
                                        AudienceMembershipEntity.join(
                                                contactEntity.getId(),
                                                id))
                                .toList();
                audienceMembershipRepository.saveAll(memberships);
            }
            return contactEntity;
        });
    }

    private void validateStaticAudiences(Set<AudienceId> audienceIds) {
        audienceMembershipValidator.validateManualMembership(audienceIds);
    }
}
