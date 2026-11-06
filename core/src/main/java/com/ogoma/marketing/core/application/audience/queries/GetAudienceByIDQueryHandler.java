package com.ogoma.marketing.core.application.audience.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.exceptions.RecordNotFoundException;
import org.jspecify.annotations.NonNull;

public record GetAudienceByIDQueryHandler(AudienceRepository audienceRepository) implements QueryHandler<GetAudienceByIDQuery,GetAudienceByIDView> {
    @Override
    public @NonNull Class<GetAudienceByIDQuery> supports() {
        return GetAudienceByIDQuery.class;
    }

    @Override
    public GetAudienceByIDView handle(@NonNull GetAudienceByIDQuery query) {
        return audienceRepository.findById(query.audienceId())
                .map(GetAudienceByIDView::new)
                .orElseThrow(()->new RecordNotFoundException("Audience not found"));
    }
}
