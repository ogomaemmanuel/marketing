package com.ogoma.marketing.infrastructure.converters;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ReadingConverter
public class StronglyTypedIdReadingConverter implements GenericConverter {

    private final Map<Class<?>, Constructor<?>> cache = new ConcurrentHashMap<>();

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(new ConvertiblePair(UUID.class, TypedID.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) return null;
        Class<?> targetClass = targetType.getType();
        Constructor<?> ctor = cache.computeIfAbsent(targetClass, c -> {
            try {
                return c.getDeclaredConstructor(UUID.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(
                        "No (UUID) constructor found on " + c, e);
            }
        });
        try {
            return ctor.newInstance(source);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate " + targetClass, e);
        }
    }
}
