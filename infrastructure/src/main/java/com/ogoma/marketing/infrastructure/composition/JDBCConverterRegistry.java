package com.ogoma.marketing.infrastructure.composition;

import com.ogoma.marketing.infrastructure.converters.*;
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
                new EncryptedStringFieldToStringWritingConverter(),
                new RuleSetToJsonBWritingConverter(),
                new JsonToRulesetReadingConverter(),
                new StronglyTypedIdWritingConverter(),
                new StronglyTypedIdReadingConverter()
        );
    }
}
