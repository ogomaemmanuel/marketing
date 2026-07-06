package com.ogoma.marketing.core.implementations;

import com.ogoma.marketing.core.abstractions.Message;
import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.MessageSenderService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MessageRouterImpl implements MessageRouter {
    private Map<Class<? extends Message>, MessageSenderService<? extends Message>> messageSenderServiceMap = new ConcurrentHashMap<>();

    public MessageRouterImpl(List<MessageSenderService<? extends Message>> messageSenderServices) {

        for (MessageSenderService<? extends Message> sender : messageSenderServices
        ) {
            this.messageSenderServiceMap.put(sender.supports(), sender);
        }

    }

    @Override
    public <M extends Message> void route(M message) {
        MessageSenderService<? extends Message> sender = messageSenderServiceMap.get(message.getClass());
        if (sender == null) {
            log.error(" Message Sender not found for {}", message.getClass().getSimpleName());
            throw new IllegalStateException(String.format("Message sender not found for %s", message.getClass().getSimpleName()));
        }
        executeSend(sender, message);
    }

    @SuppressWarnings("unchecked")
    private <M extends Message> void executeSend(MessageSenderService<? extends Message> sender, M message) {
        ((MessageSenderService<M>) sender).send(message);
    }
}
