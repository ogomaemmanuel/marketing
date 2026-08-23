package com.ogoma.marketing.api.transactionmessages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ogoma.marketing.core.abstractions.Command;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @JsonSubTypes.Type(value = SmsTransactionalMessageRequest.class, name = "sms",names = {"SMS"}),
        @JsonSubTypes.Type(value = EmailTransactionalMessageRequest.class, name = "email",names = {"EMAIL"})
}
)
@Getter
@Setter
public abstract sealed class TransactionalMessageRequestBase permits EmailTransactionalMessageRequest,
        SmsTransactionalMessageRequest {
    @Schema(description = "Optional datetime string for scheduling the delivery of message in iso 8601 format ")
    private ZonedDateTime scheduledAt;

    public abstract Command<Void> asCommand(String userId);


}
