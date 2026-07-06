package com.ogoma.marketing.api.transactionmessages;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactional-messages")
public class TransactionalMessageSenderController {

    private final CommandDispatcher commandDispatcher;

    public TransactionalMessageSenderController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public Void sendMessage(
            @Valid @RequestBody TransactionalMessageRequestBase transactionalMessageRequest,
            @CurrentUser String userId
    ) {
        return this.commandDispatcher.dispatch(transactionalMessageRequest.asCommand(userId));
    }
}
