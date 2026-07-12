package com.ogoma.marketing.api.audience;

import com.ogoma.marketing.core.application.audience.commands.AddAudienceCommand;

public record AddAudienceRequest(String name) {

    AddAudienceCommand toCommand(String userId) {
        return new AddAudienceCommand(name, userId);
    }
}
