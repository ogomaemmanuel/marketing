package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.sharedkernel.EncryptedStringField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToEncryptedStringReadingConverter implements Converter<String, EncryptedStringField> {
    @Override
    public EncryptedStringField convert(String source) {
        return new EncryptedStringField(source);
    }
}
