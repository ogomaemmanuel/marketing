package com.ogoma.marketing.api.contacts;

import com.ogoma.marketing.core.domain.contacts.ContactID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToContactIDConverter implements Converter<String, ContactID> {
    @Override
    public ContactID convert(String source) {
        return new ContactID(UUID.fromString(source));
    }
}
