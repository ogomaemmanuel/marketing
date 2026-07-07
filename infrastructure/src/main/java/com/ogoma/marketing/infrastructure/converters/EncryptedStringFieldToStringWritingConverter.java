package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.sharedkernel.EncryptedStringField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class EncryptedStringFieldToStringWritingConverter implements Converter<EncryptedStringField,String> {
    @Override
    public String convert(EncryptedStringField source) {
        return source.value();
    }
}
