package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public record ExportContactsCommandHandler(
        ContactRepository contactRepository
) implements CommandHandler<ExportContactsCommand, Stream<ExportContactsView>> {
    @Override
    public @NonNull Class<ExportContactsCommand> supports() {
        return ExportContactsCommand.class;
    }

    @Override
    public Stream<ExportContactsView> handle(@NonNull ExportContactsCommand command) {
        return contactRepository.findAllByAudienceIds(command.audienceIds());
    }
}
