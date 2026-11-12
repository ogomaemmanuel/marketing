package com.ogoma.marketing.core.domain.audience;

import java.util.Optional;

public interface TestAudienceRepository {
    TestAudience save(TestAudience testAudience);
    TestAudience delete(TestAudience testAudience);
    Optional<TestAudience> findFirst();
    Optional<TestAudience> findByAudienceId(AudienceId audienceId);
}
