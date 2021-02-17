package org.ghotibeaun.json.converters.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.Converters;
import org.ghotibeaun.json.converters.utils.ScannerEntry;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public abstract class MemberHandler<M extends Member> {

    private final M member;
    private final Optional<ValueConverter<?>> valueConverter;
    private final ScannerEntry entry;

    public MemberHandler(M member, ScannerEntry entry, Optional<ValueConverter<?>> valueConverter) {
        this.member = member;
        this.entry = entry;
        this.valueConverter = valueConverter;
    }

    public static MemberHandler<? extends Member> newMemberHandler(Member member, ScannerEntry entry, Optional<ValueConverter<?>> valueConverter) {
        if (member instanceof Field) {
            return new FieldMemberHandler((Field)member, entry, valueConverter);
        } else {
            return new MethodMemberHandler((Method)member, entry, valueConverter);
        }
    }

    public M getMember() {
        return member;
    }

    public ScannerEntry getEntry() {
        return this.entry;
    }

    public Optional<ValueConverter<?>> getValueConverter() {
        return valueConverter;
    }

    public Optional<Object> handleJSONValue(JSONValue<?> value) throws JSONConversionException {
        Object returnValue = null;

        if (entry.isIgnore()) {
            return Optional.empty();
        }

        if (getValueConverter().isPresent()) {
            returnValue = getValueConverter().get().convertValue(value.getValue());
        } else if (value.isPrimitive()) {
            returnValue = value.getValue();
        } else {
            final Class<?> targetClass = entry.getTargetClass();
            if (value.isArray()) {
                returnValue = Converters.convertToList(targetClass, (JSONArray)value.getValue(), getValueConverter());
            } else {
                returnValue = Converters.convertToClass(targetClass, (JSONObject)value.getValue());
            }
        }

        return Optional.ofNullable(returnValue);
    }

    public abstract void setMemberValue(Object instance, JSONValue<?> value) throws JSONConversionException;

    public abstract <V> V getMemberValue(Object instance) throws JSONConversionException;


    public JSONValue<?> getJSONValue(Object instance) throws JSONConversionException {
        final Object val = getMemberValue(instance);
        return Converters.convertValue(val, Optional.empty(), Optional.empty());
    }

}
