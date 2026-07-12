package com.ogoma.marketing.core.application.audience.queries;

import com.ogoma.marketing.core.abstractions.QueryHandler;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import org.springframework.data.domain.Page;

public record GetAudiencesQueryHandler(
        AudienceRepository audienceRepository) implements QueryHandler<GetAudiencesQuery, Page<GetAudiencesView>> {


    @Override
    public Class<GetAudiencesQuery> supports() {
        return GetAudiencesQuery.class;
    }

    @Override
    public Page<GetAudiencesView> handle(GetAudiencesQuery query) {
        return this.audienceRepository.findAllBy(query.searchTerm(),query.pageable()).map(GetAudiencesView::new);
    }
}
