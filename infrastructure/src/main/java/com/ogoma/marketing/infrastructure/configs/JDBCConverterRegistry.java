package com.ogoma.marketing.infrastructure.configs;

import com.ogoma.marketing.infrastructure.converters.EmailTemplateToJsonWritingConverter;
import com.ogoma.marketing.infrastructure.converters.EncryptedStringFieldToStringWritingConverter;
import com.ogoma.marketing.infrastructure.converters.JsonToEmailTemplateReadingConverter;
import com.ogoma.marketing.infrastructure.converters.StringToEncryptedStringReadingConverter;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Configuration
public class JDBCConverterRegistry extends AbstractJdbcConfiguration {
    @Override
    @NullMarked
    protected List<?> userConverters() {
        return List.of(
                new EmailTemplateToJsonWritingConverter(),
                new JsonToEmailTemplateReadingConverter(),
                new StringToEncryptedStringReadingConverter(),
                new EncryptedStringFieldToStringWritingConverter()
        );
    }
}
