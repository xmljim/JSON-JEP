package org.ghotibeaun.json.converters.handlers;

import java.lang.reflect.Field;
import java.util.Optional;

import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.utils.ScannerEntry;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public class FieldMemberHandler extends MemberHandler<Field> {

    public FieldMemberHandler(Field member, ScannerEntry entry, Optional<ValueConverter<?>> valueConverter) {
        super(member, entry, valueConverter);
    }

    @Override
    public void setMemberValue(Object instance, JSONValue<?> value) throws JSONConversionException {
        final Optional<Object> valueToSet = handleJSONValue(value);

        if (valueToSet.isPresent()) {
            try {
                getMember().setAccessible(true);
                getMember().set(instance, valueToSet.get());
            } catch (final Exception e) {
                throw new JSONConversionException(e);
            }
        }        
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getMemberValue(Object instance) throws JSONConversionException {
        try {
            getMember().setAccessible(true);
            final Object rawValue = getMember().get(instance);

            if (getValueConverter().isPresent()) {
                return (V) getValueConverter().get().convertValue(rawValue);
            } else {
                return (V) rawValue;
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new JSONConversionException(e.getMessage(), e);
        }
    }



}
