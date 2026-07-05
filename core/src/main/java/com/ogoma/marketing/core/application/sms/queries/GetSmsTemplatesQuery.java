package com.ogoma.marketing.core.application.sms.queries;

import com.ogoma.marketing.core.abstractions.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record GetSmsTemplatesQuery(String searchTerm, Pageable pageable) implements Query<Page<GetSmsTemplatesView>> {
}
