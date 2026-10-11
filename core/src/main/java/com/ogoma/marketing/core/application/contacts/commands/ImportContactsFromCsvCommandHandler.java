package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.abstractions.UnitOfWork;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipEntity;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ImportContactsFromCsvCommandHandler(
        AudienceMembershipValidator audienceMembershipValidator,
        UnitOfWork unitOfWork,
        ContactRepository contactRepository,
        AudienceMembershipRepository audienceMembershipRepository
) implements CommandHandler<ImportContactsFromCsvCommand, Mono<Void>> {


    @Override
    public Class<ImportContactsFromCsvCommand> supports() {
        return ImportContactsFromCsvCommand.class;
    }

    @Override
    public Mono<Void> handle(ImportContactsFromCsvCommand command) {
        return Mono.fromRunnable(() -> {
            if (command.contactDetails().isEmpty()) {
                return;
            }
            unitOfWork.execute(() -> {
                audienceMembershipValidator.validateManualMembership(command.audienceIds());
                List<ContactEntity> contacts = command.contactDetails().stream().map(details -> ContactEntity.createNew(
                        details.firstName(),
                        details.lastName(),
                        details.email(),
                        details.phoneNumber(), Map.of(), command.userId())).toList();
                this.contactRepository.saveAll(contacts);
                if (!command.audienceIds().isEmpty()) {
                    List<AudienceMembershipEntity> audienceMembershipList = new ArrayList<>(contacts.size() * command.audienceIds().size());
                    for (var contact : contacts) {
                        for (var audienceId : command.audienceIds()) {
                            audienceMembershipList.add(AudienceMembershipEntity.join(contact.getId(), audienceId));
                        }
                    }
                    audienceMembershipRepository.saveAll(audienceMembershipList);
                }
            });
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
