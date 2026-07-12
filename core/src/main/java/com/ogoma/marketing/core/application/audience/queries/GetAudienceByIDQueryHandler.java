package com.ogoma.marketing.core.application.audience.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.audience.AudienceEntity;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;

public record GetAudienceByIDQueryHandler(AudienceRepository audienceRepository) implements QueryHandler<GetAudienceByIDQuery,GetAudienceByIDView> {
    @Override
    public Class<GetAudienceByIDQuery> supports() {
        return GetAudienceByIDQuery.class;
    }

    @Override
    public GetAudienceByIDView handle(GetAudienceByIDQuery query) {
        return audienceRepository.findById(query.audienceId())
                .map(GetAudienceByIDView::new)
                .orElseThrow(()->new RecordNotFoundException("Audience not found"));
    }
}
