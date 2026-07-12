package com.ogoma.marketing.core.application.audience.commands;

import com.ogoma.marketing.core.abstractions.Command;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;

public record AddAudienceCommand(
        String name,
        String userID
)  implements Command<AudienceEntity> {

}
