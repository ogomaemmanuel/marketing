package com.ogoma.marketing.infrastructure.converters;


import com.ogoma.marketing.core.domain.outbox.Payload;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import tools.jackson.databind.json.JsonMapper;

@ReadingConverter
public class OutboxPayloadReadingConverter implements Converter<PGobject, Payload> {
    @Override
    public Payload convert(PGobject source) {
        return JsonMapper.shared().readValue(source.getValue(), Payload.class);
    }
}
