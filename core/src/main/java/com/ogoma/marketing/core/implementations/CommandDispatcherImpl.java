package com.ogoma.marketing.core.implementations;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.CommandHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandDispatcherImpl implements CommandDispatcher {

    private final Map<Class<? extends Command<?>>, CommandHandler<? extends Command<?>, ?>> commandHandlerRegistry = new ConcurrentHashMap<>();

    public CommandDispatcherImpl(List<? extends CommandHandler<? extends Command<?>, ?>> commandHandlers) {
        commandHandlers.forEach(commandHandler ->
                commandHandlerRegistry.put(commandHandler.supports(), commandHandler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R> R dispatch(C command) {
        return ((CommandHandler<C, R>) this.commandHandlerRegistry.get(command.getClass()))
                .handle(command);

    }
}
