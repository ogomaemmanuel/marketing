package com.ogoma.marketing.infrastructure.converters;

import org.postgresql.util.PGobject;
import org.springframework.data.jdbc.core.mapping.JdbcValue;
import tools.jackson.databind.json.JsonMapper;

import java.sql.JDBCType;

public class JdbcValueConverter {
    private JdbcValueConverter() {
    }

    public static JdbcValue convert(Object source) {
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
