package com.ogoma.marketing.api.polls;

import com.ogoma.marketing.core.application.polls.CreatePollCommand;
import com.ogoma.marketing.core.domain.polls.valueobjects.ListStyle;
import com.ogoma.marketing.core.domain.polls.valueobjects.Orientation;

import java.util.Set;

public record CreatePollRequest(
        String name,
        String question,
        String questionDescription,
        String redirectURL,
        Set<String> options,
        Orientation orientation,
        ListStyle listStyle
) {
    CreatePollCommand toCommand(String user) {
        return new CreatePollCommand(name, question, questionDescription, redirectURL, options, orientation, listStyle, user);
    }
}
