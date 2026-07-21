package com.ogoma.marketing.api.users;

import com.ogoma.marketing.api.resolvers.CurrentUserDetails;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.application.users.SyncUserInfoCommand;
import com.ogoma.marketing.core.domain.users.UserID;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public record UsersController(CommandDispatcher commandDispatcher) {
    @PostMapping("/sync")
    @Operation(
            summary = "Synchronise user info",
            description = "Synchronises user info upon successful login eg on next ouath signup callback. It does upsert operation"
    )
    public UserID syncUserInfo(CurrentUserDetails currentUserDetails) {
        return this.commandDispatcher.dispatch(new SyncUserInfoCommand(
                currentUserDetails.email(),
                currentUserDetails.firstName(),
                currentUserDetails.lastName(),
                currentUserDetails.externalId()
        ));
    }
}
