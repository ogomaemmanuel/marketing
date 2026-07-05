package com.ogoma.marketing.infrastructure;


import com.ogoma.marketing.core.abstractions.UnitOfWork;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
public class SpringTransactionUnitOfWork implements UnitOfWork {

    private final TransactionTemplate transactionTemplate;

    // Spring will automatically inject the PlatformTransactionManager into the TransactionTemplate
    public SpringTransactionUnitOfWork(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public <T> T execute(Supplier<T> action) {
        // Imperatively executes the supplier logic within a transaction.
        // If a RuntimeException occurs, Spring automatically rolls it back.
        return transactionTemplate.execute(status -> action.get());
    }

    @Override
    public void execute(Runnable action) {
        transactionTemplate.executeWithoutResult(status -> action.run());
    }
}