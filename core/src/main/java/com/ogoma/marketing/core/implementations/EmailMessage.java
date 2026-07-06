package com.ogoma.marketing.core.implementations;

import com.ogoma.marketing.core.abstractions.Message;

import java.util.List;

public record EmailMessage(
        String subject,

        String content,
        List<String> recipients

) implements Message {
}
