package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;
import tools.jackson.databind.json.JsonMapper;

import java.sql.JDBCType;

@WritingConverter
public class EmailTemplateToJsonWritingConverter implements Converter<EmailTemplate, JdbcValue> {
    @Override
    public JdbcValue convert(EmailTemplate source) {
        try {
            String json = JsonMapper.shared().writeValueAsString(source);
            // For PostgreSQL JSONB support:
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(json);
            return JdbcValue.of(pgObject, JDBCType.OTHER);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ProductMetadata to JSON", e);
        }
    }
}
