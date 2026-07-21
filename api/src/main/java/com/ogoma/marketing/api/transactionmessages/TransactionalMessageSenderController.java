package com.ogoma.marketing.api.transactionmessages;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactional-messages")
public record TransactionalMessageSenderController(CommandDispatcher commandDispatcher) {
    @PostMapping
    @Operation(
            summary = "send transactional sms/email messages",
            description = "Transactional messages are are automated messages triggered by user actions eg password reset, account creation etc"
    )
    public Void sendMessage(
            @Valid @RequestBody TransactionalMessageRequestBase transactionalMessageRequest,
            @CurrentUser String userId
    ) {
        return this.commandDispatcher.dispatch(transactionalMessageRequest.asCommand(userId));
    }
}
