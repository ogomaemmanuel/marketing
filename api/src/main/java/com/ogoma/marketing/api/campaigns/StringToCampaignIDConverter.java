package com.ogoma.marketing.api.campaigns;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToCampaignIDConverter implements Converter<String, CampaignID> {
    @Override
    public CampaignID convert(String source) {
        return new CampaignID(UUID.fromString(source));
    }
}
