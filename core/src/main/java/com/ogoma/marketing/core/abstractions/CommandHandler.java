package com.ogoma.marketing.core.abstractions;

import org.jspecify.annotations.NonNull;

public interface CommandHandler<T extends Command<R>, R> {

   @NonNull Class<T> supports();

    R handle(@NonNull T command);
}
