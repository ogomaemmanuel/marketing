package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;

import java.util.stream.Stream;

public record ExportContactsCommandHandler(
        ContactRepository contactRepository
) implements CommandHandler<ExportContactsCommand, Stream<ExportContactsView>> {
    @Override
    public Class<ExportContactsCommand> supports() {
        return ExportContactsCommand.class;
    }

    @Override
    public Stream<ExportContactsView> handle(ExportContactsCommand command) {
        return contactRepository.findAllByAudienceIds(command.audienceIds());
    }
}
