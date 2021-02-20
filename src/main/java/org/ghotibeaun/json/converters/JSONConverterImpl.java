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
package org.ghotibeaun.json.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.handlers.MemberHandler;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.utils.AnnotationUtils;
import org.ghotibeaun.json.converters.utils.ClassScanner;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.converters.utils.ScannerEntry;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

class JSONConverterImpl extends AbstractJSONConverter {

    public JSONConverterImpl(Options... option) {
        super(option);

    }

    @Override
    public <T> T convertToClass(Class<T> targetClass, JSONObject json) throws JSONConversionException {
        final ClassScanner scanner = new ClassScanner(targetClass);
        final ClassScanner.Validation validation = scanner.validate(json);

        if (!validation.isValid()) {
            throw new JSONConversionException("Errors were found during Class scanning. JSON Data does not appear to match intended Class: "
                    + String.join("\n", validation.getMissingElements()));
        }

        //OK we're good now process the data
        final T instance = ClassUtils.createInstance(targetClass);
        processScan(scanner, json, instance);
        return instance;
    }

    @Override
    public <T> List<T> convertToList(JSONArray array, Optional<ValueConverter<?>> valueConverter,
            Optional<Class<?>> targetClass) throws JSONConversionException {
        final List<T> newList = new ArrayList<>();

        for (final JSONValue<?> value : array) {
            newList.add(convertValue(value, valueConverter, targetClass));
        }

        return newList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convertValue(JSONValue<?> value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass)
            throws JSONConversionException {

        if (valueConverter.isPresent() && targetClass.isPresent()) {
            throw new JSONConversionException("Can specify a ValueConverter or targetClass, but not both");
        }

        if (value == null) {
            return null;
        } else if (valueConverter.isPresent()) {
            return (T) valueConverter.get().convertValue(value.getValue());
        } else if (value.isPrimitive()) {
            return (T) value.getValue();
        } else if (value.isArray()) {
            return (T) convertToList((JSONArray)value.getValue(), valueConverter, targetClass);
        } else if (value.isObject()) {
            return (T) convertToClass(targetClass.orElse(Object.class), (JSONObject)value.getValue());
        } else {
            throw new JSONConversionException("Unexpected scenario. Contact Jim Earley -> xml.jim@gmail.com");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public <T> List<T> convertToList(Class<T> targetItemClass, JSONArray array,
            Optional<ValueConverter<?>> valueConverter) throws JSONConversionException {

        final List<T> newList = new ArrayList<>();

        for (final JSONValue<?> value : array) {
            if (valueConverter.isPresent()) {
                newList.add((T) valueConverter.get().convertValue(value.getValue()));
            } else if (value.isPrimitive()) {
                newList.add((T) value.getValue());
            } else if (value.isObject()) {
                newList.add(Converters.convertToClass(targetItemClass, (JSONObject)value.getValue()));
            } else {

            }
        }

        return newList;
    }

    private void processScan(ClassScanner scanner, JSONObject context, Object instance) throws JSONConversionException {
        scanner.entries().forEach(entry -> processEntry(entry, context, instance));
    }

    private void processEntry(ScannerEntry entry, JSONObject context, Object instance) throws JSONConversionException {
        final Optional<MemberHandler<?>> memberHandler = getMemberHandler(entry, instance);

        if (memberHandler.isPresent()) {
            memberHandler.get().setMemberValue(instance, context.get(entry.getJsonKey()));
        } 
    }

    private Optional<MemberHandler<?>> getMemberHandler(ScannerEntry entry, Object instance) throws JSONConversionException {

        MemberHandler<?> handler = null;

        if (!entry.isIgnore()) {
            if (entry.getContainerClass().equals(instance.getClass())) {
                try {
                    final Field field = instance.getClass().getDeclaredField(entry.getFieldName());
                    final Optional<ValueConverter<?>> valueConverter = AnnotationUtils.getJSONValueConverter(field);
                    handler = MemberHandler.newMemberHandler(field, entry, valueConverter);
                } catch (NoSuchFieldException | SecurityException e) {
                    throw new JSONConversionException("Error retrieving field [" + entry.getFieldName() + "] from class "
                            + entry.getContainerClass().getName() + "]", e);
                }
            } else {
                final Optional<Method> method = entry.getSetterMethod();
                if (method.isPresent()) {
                    final Optional<ValueConverter<?>> valueConverter = AnnotationUtils.getJSONValueConverter(method.get());
                    handler = MemberHandler.newMemberHandler(method.get(), entry, valueConverter);
                } else {
                    throw new JSONConversionException("Could not create setter method for " + entry.getJsonKey());
                }
            }
        }
        return Optional.ofNullable(handler);
    }



}
