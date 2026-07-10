package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.abstractions.Message;
import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendMessageActivity;
import org.springframework.stereotype.Component;

@Component
public class SendMessageActivityImpl implements SendMessageActivity {
    private final MessageRouter router;

    public SendMessageActivityImpl(MessageRouter router) {
        this.router = router;
    }

    @Override
    public void send(Message message) {
        this.router.route(message);
    }
}
