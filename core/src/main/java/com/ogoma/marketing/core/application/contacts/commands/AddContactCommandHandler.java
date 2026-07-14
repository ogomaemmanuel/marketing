package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipEntity;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            Set<AudienceId> audienceIds = command.audienceIds() == null
                    ? Set.of()
                    : command.audienceIds().stream()
                    .map(AudienceId::new)
                    .collect(Collectors.toSet());
            validateStaticAudiences(audienceIds);
            ContactEntity contact =
                    ContactEntity.createNew(
                            command.firstName(),
                            command.lastName(),
                            command.email(),
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
        if (audienceIds.isEmpty()) {
            return;
        }
        Set<AudienceId> staticIds = audienceRepository.findStaticAudienceIds(audienceIds);
        Set<AudienceId> invalidIds =
                new HashSet<>(audienceIds);
        invalidIds.removeAll(staticIds);
        if (!invalidIds.isEmpty()) {
            throw new InvalidAudienceException(
                    "Invalid audiences: " + invalidIds);
        }
    }
}
