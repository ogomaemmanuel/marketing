package com.ogoma.marketing.core.application.contacts.commands;

import java.util.UUID;

public record ExportContactsView(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
        ) {
}
