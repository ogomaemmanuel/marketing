package com.ogoma.marketing.api.audience;

import com.ogoma.marketing.core.application.audience.commands.UpdateAudienceCommand;
import com.ogoma.marketing.core.domain.audience.AudienceId;

public record UpdateAudienceRequest(String name) {
    public UpdateAudienceCommand toCommand(AudienceId audienceId,String userId){
        return new UpdateAudienceCommand(audienceId,name,userId);
    }
}
