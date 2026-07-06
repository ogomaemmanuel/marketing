package com.ogoma.marketing.api.transactionmessages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ogoma.marketing.core.abstractions.Command;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "channel",
        visible = true

)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SmsTransactionalMessageRequest.class, name = "sms"),
        @JsonSubTypes.Type(value = EmailTransactionalMessageRequest.class, name = "email")
}
)
public abstract sealed class TransactionalMessageRequestBase permits EmailTransactionalMessageRequest,
        SmsTransactionalMessageRequest {

    public abstract Command<Void> asCommand(String userId);


}
