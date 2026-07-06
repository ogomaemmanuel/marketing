package com.ogoma.marketing.api.transactionmessages;

import com.ogoma.marketing.core.application.transactionalmessages.SendSmsTransactionalCommand;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;


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
                Map.copyOf(params)
        );
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public UUID getTemplatedId() {
        return templatedId;
    }

    public void setTemplatedId(UUID templatedId) {
        this.templatedId = templatedId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
