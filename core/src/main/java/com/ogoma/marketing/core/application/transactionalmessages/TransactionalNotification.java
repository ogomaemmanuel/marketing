package com.ogoma.marketing.core.application.transactionalmessages;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type",
        visible = true
)
@JsonSubTypes(
        value = {
                @JsonSubTypes.Type(value = SendSmsTransactionalCommand.class, name = "sms"),
                @JsonSubTypes.Type(value = SendTransactionalEmailCommand.class, name = "email")
        }
)
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface TransactionalNotification permits SendTransactionalEmailCommand, SendSmsTransactionalCommand {
}
