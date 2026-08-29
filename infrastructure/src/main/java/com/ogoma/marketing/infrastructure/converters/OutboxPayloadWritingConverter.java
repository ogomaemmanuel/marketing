package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.domain.outbox.Payload;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;

@WritingConverter
public class OutboxPayloadWritingConverter implements Converter<Payload, JdbcValue> {
    @Override
    public JdbcValue convert(Payload source) {
        return JdbcValueConverter.convert(source);
    }
}
