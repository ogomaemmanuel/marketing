package com.ogoma.marketing.api.transactionmessages;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.application.transactionalmessages.SendTransactionalEmailCommand;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public non-sealed class EmailTransactionalMessageRequest extends TransactionalMessageRequestBase {

    @NotEmpty
    private List<String> recipients;
    @NotNull
    private UUID templatedId;

    @NotNull
    private Map<String, Object> params;

    @Override
    public Command<Void> asCommand(String userID) {
        return new SendTransactionalEmailCommand(
                new EmailTemplateID(templatedId),
                recipients,
                Map.copyOf(params)
        );
    }
}
