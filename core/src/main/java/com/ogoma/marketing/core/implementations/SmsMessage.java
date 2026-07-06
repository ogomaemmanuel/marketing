package com.ogoma.marketing.core.implementations;

import com.ogoma.marketing.core.abstractions.Message;


public record SmsMessage(
        String recipient,
        String content
) implements Message {


}
