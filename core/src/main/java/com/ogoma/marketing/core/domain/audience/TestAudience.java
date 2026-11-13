package com.ogoma.marketing.core.domain.audience;


import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "test_audience")
@Getter
public class TestAudience{
    private AudienceId audienceId;

    public TestAudience(AudienceId audienceId) {
        this.audienceId = audienceId;
    }
}
