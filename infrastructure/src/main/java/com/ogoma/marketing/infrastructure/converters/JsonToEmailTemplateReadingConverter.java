package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;

import org.springframework.data.convert.ReadingConverter;
import tools.jackson.databind.json.JsonMapper;

@ReadingConverter
public class JsonToEmailTemplateReadingConverter implements Converter<PGobject, EmailTemplate> {
    @Override
    public EmailTemplate convert(PGobject source) {
        return JsonMapper.shared().readValue(source.getValue(), EmailTemplate.class);
    }
}
