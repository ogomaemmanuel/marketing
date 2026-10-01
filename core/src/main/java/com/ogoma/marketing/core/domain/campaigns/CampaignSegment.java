package com.ogoma.marketing.core.domain.campaigns;


import com.ogoma.marketing.core.domain.segments.SegmentID;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

@Table("campaign_segments")
public record CampaignSegment(
        SegmentID segmentId
) {

    public CampaignSegment{
        Assert.notNull(segmentId,"%s segment id is require".formatted(CampaignSegment.class.getSimpleName()));
    }
}
