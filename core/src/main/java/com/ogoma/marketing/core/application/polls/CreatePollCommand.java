package com.ogoma.marketing.core.application.polls;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.polls.valueobjects.ListStyle;
import com.ogoma.marketing.core.domain.polls.valueobjects.Orientation;
import com.ogoma.marketing.core.domain.polls.valueobjects.PollID;

import java.util.Set;

public record CreatePollCommand(
        String name,
        String question,
        String questionDescription,
        String redirectURL,
        Set<String> options,
        Orientation orientation,
        ListStyle listStyle,
        String createdBy
) implements Command<PollID> {
    public CreatePollCommand {
        options = options == null ? Set.of() : Set.copyOf(options);
    }
}
