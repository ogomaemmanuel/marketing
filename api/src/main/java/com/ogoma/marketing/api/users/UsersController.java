package com.ogoma.marketing.api.users;

import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.application.users.SyncUserInfoCommand;
import com.ogoma.marketing.core.domain.users.UserID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public record UsersController(CommandDispatcher commandDispatcher) {
    @PostMapping("/sync")
    public UserID syncUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return this.commandDispatcher.dispatch(new SyncUserInfoCommand(
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                jwt.getClaimAsString("sub")
        ));
    }
}
