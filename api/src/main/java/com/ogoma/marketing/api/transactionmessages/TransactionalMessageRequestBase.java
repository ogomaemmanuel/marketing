package com.ogoma.marketing.api.transactionmessages;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ogoma.marketing.core.abstractions.Command;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

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
@Getter
@Setter
public abstract sealed class TransactionalMessageRequestBase permits EmailTransactionalMessageRequest,
        SmsTransactionalMessageRequest {
    @JsonPropertyDescription("Optional datetime string for scheduling the delivery of message in iso 8601 format ")
    private ZonedDateTime scheduledAt;

    public abstract Command<Void> asCommand(String userId);


}
