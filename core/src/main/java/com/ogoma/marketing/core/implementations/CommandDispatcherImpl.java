package com.ogoma.marketing.core.implementations;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.CommandHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CommandDispatcherImpl implements CommandDispatcher {

    private final Map<Class<? extends Command<?>>, CommandHandler<? extends Command<?>, ?>> commandHandlerRegistry = new ConcurrentHashMap<>();

    public CommandDispatcherImpl(List<? extends CommandHandler<? extends Command<?>, ?>> commandHandlers) {
        commandHandlers.forEach(commandHandler ->
                commandHandlerRegistry.put(commandHandler.supports(), commandHandler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R> R dispatch(C command) {
        Objects.requireNonNull(command);
        var handler = ((CommandHandler<C, R>) this.commandHandlerRegistry.get(command.getClass()));
        if (handler == null) {
            log.error("Handler not found for command {}", command);
            throw new IllegalStateException(String.format("Command Handler not for %s", command.getClass().getSimpleName()));
        }
        return handler.handle(command);
    }
}
