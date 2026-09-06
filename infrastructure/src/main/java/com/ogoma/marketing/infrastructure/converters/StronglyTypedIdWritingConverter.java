package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Set;
import java.util.UUID;

@WritingConverter
public class StronglyTypedIdWritingConverter implements GenericConverter {

    @Override
    @Nullable
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(new ConvertiblePair(TypedID.class, UUID.class));
    }

    @Override
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return source == null ? null : ((TypedID<?>) source).id();
    }
}