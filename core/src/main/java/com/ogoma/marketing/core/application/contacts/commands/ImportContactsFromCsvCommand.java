package com.ogoma.marketing.core.application.contacts.commands;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public record ImportContactsFromCsvCommand(
        List<ContactDetails> contactDetails,
        Set<AudienceId> audienceIds,
        String userId
) implements Command<Mono<Void>> {
    @JsonPropertyOrder({"email", "firstName", "lastName", "phoneNumber"})
    public record ContactDetails(String email,String firstName, String lastName, String phoneNumber) {

    }
}
