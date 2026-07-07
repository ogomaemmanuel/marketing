package com.ogoma.marketing.core.application.users;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.users.UserID;

public record SyncUserInfoCommand(
        String email,
        String firstName,
        String lastName,
        String externalId
) implements Command<UserID> {

}
