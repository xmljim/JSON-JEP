/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
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
                returnValue = Converters.convertToList((JSONArray)value.getValue(), Optional.empty(), Optional.of(targetClass));
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
        return Converters.convertToJSONValue(val, Optional.empty(), Optional.empty());
    }

}
