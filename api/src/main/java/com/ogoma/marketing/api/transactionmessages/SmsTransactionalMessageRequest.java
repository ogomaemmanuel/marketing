package com.ogoma.marketing.api.transactionmessages;

import com.ogoma.marketing.core.application.transactionalmessages.SendSmsTransactionalCommand;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public final class SmsTransactionalMessageRequest extends TransactionalMessageRequestBase {
    @NotBlank
    private String recipient;
    @NotNull
    private UUID templatedId;

    @NotNull
    private Map<String, Object> params;

    @Override
    public SendSmsTransactionalCommand asCommand(String userID) {
        return new SendSmsTransactionalCommand(
                this.recipient,
                new SmsTemplateID(this.templatedId),
                Map.copyOf(params),
                this.getScheduledAt()
        );
    }
}
