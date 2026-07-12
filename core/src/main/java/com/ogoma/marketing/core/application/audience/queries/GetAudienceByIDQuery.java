package com.ogoma.marketing.core.application.audience.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.audience.AudienceId;

public record GetAudienceByIDQuery (AudienceId audienceId) implements Query<GetAudienceByIDView> {
}
