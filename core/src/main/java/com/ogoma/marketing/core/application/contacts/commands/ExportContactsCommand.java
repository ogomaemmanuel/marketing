package com.ogoma.marketing.core.application.contacts.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceId;

import java.util.Set;
import java.util.stream.Stream;

public record ExportContactsCommand(Set<AudienceId> audienceIds, String userId) implements Command<Stream<ExportContactsView>> {
}
