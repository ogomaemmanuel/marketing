package com.ogoma.marketing.core.abstractions;

public interface CommandHandler<T extends Command<R>, R> {

    Class<T> supports();

    R handle(T command);
}
