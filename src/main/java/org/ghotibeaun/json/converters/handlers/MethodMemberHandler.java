package org.ghotibeaun.json.converters.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.utils.ScannerEntry;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public class MethodMemberHandler extends MemberHandler<Method> {

    public MethodMemberHandler(Method member, ScannerEntry entry, Optional<ValueConverter<?>> valueConverter) {
        super(member, entry, valueConverter);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setMemberValue(Object instance, JSONValue<?> value) throws JSONConversionException {
        final Optional<Object> valueToSet = handleJSONValue(value);

        if (valueToSet.isPresent()) {
            try {
                getMember().setAccessible(true);
                getMember().invoke(instance, valueToSet.get());
            } catch (final Exception e) {
                throw new JSONConversionException(e);
            }
        }         
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getMemberValue(Object instance) throws JSONConversionException {
        try {
            final Object rawValue = getMember().invoke(instance);

            if (getValueConverter().isPresent()) {
                return (V) getValueConverter().get().convertValue(rawValue);
            } else {
                return (V) rawValue;
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new JSONConversionException(e.getMessage(), e);
        }
    }



}
