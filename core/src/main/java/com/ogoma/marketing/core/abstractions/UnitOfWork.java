package com.ogoma.marketing.core.abstractions;

import java.util.function.Supplier;

public interface UnitOfWork {
    /**
     * Executes a block of code that returns a value within a transactional boundary.
     */
    <T> T execute(Supplier<T> action);

    /**
     * Executes a block of code that does not return a value within a transactional boundary.
     */
    void execute(Runnable action);
}
