package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.domain.audience.RuleSet;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import tools.jackson.databind.json.JsonMapper;

@ReadingConverter
public class JsonToRulesetReadingConverter implements Converter<PGobject, RuleSet> {
    @Override
    public RuleSet convert(PGobject source) {
        return JsonMapper.shared().readValue(source.getValue(), RuleSet.class);
    }
}
