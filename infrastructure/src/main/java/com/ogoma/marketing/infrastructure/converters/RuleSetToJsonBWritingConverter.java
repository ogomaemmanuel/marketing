package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.domain.audience.RuleSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;


@WritingConverter
public class RuleSetToJsonBWritingConverter implements Converter<RuleSet, JdbcValue> {
    @Override
    public JdbcValue convert(RuleSet source) {
        return JdbcValueConverter.convert(source);
    }
}
