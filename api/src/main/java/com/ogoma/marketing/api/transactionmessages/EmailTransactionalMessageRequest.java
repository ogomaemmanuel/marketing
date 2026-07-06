package com.ogoma.marketing.api.transactionmessages;

import com.ogoma.marketing.core.abstractions.Command;

public non-sealed class EmailTransactionalMessageRequest extends TransactionalMessageRequestBase{
    @Override
   public Command<Void> asCommand(String userID) {
        return null;
    }
}
