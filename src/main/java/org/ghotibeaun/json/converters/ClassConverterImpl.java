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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.converters.handlers.MemberHandler;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.utils.AnnotationUtils;
import org.ghotibeaun.json.converters.utils.ClassScanner;
import org.ghotibeaun.json.converters.utils.ScannerEntry;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.NodeFactory;

class ClassConverterImpl extends AbstractClassConverter {

    public ClassConverterImpl(Options... option) {
        super(option);
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> JSONNode convertToJSON(T source) throws JSONConversionException {
        if (source instanceof List) {
            return convertToJSONArray((List<?>)source);
        } else if (source instanceof Map) {
            return convertToJSONObject((Map<String, ?>)source);
        } else {
            return convertToJSONObject(source);
        }
    }

    @Override
    public <T> JSONObject convertToJSONObject(T source) throws JSONConversionException {
        final ClassScanner scanner = new ClassScanner(source.getClass());
        final JSONObject json = NodeFactory.newJSONObject();
        processClass(scanner, source, json);
        return json;
    }

    @Override
    public JSONArray convertToJSONArray(List<?> source) throws JSONConversionException {
        return convertToJSONArray(source, Optional.empty(), Optional.empty());
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONArray(java.util.List, java.lang.Class)
     */
    @Override
    public JSONArray convertToJSONArray(List<?> source, Class<?> targetClass) throws JSONConversionException {
        return convertToJSONArray(source, Optional.empty(), Optional.of(targetClass));

    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONArray(java.util.List, org.ghotibeaun.json.converters.valueconverter.ValueConverter)
     */
    @Override
    public JSONArray convertToJSONArray(List<?> source, ValueConverter<?> valueConverter) throws JSONConversionException {
        return convertToJSONArray(source, Optional.of(valueConverter), Optional.empty());
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONArray(java.util.List, java.util.Optional, java.util.Optional)
     */
    @Override
    public JSONArray convertToJSONArray(List<?> source, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException {
        if (valueConverter.isPresent() && targetClass.isPresent()) {
            throw new JSONConversionException("Cannot have both a ValueConverter and TargetClass present. Choose one or the other");
        }

        final JSONArray newArray = NodeFactory.newJSONArray();
        source.stream().forEach(item -> processListItem(newArray, item, valueConverter, targetClass));
        return newArray;
    }


    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertValue(java.lang.Object, java.util.Optional, java.util.Optional)
     */
    @Override
    public JSONValue<?> convertValue(Object value, Optional<ValueConverter<?>> valueConverter,
            Optional<Class<?>> targetClass) throws JSONConversionException {

        if (valueConverter.isPresent() && targetClass.isPresent()) {
            throw new JSONConversionException("Cannot have both a ValueConverter and TargetClass present. Choose one or the other");
        }

        return createJSONValue(value, valueConverter, targetClass);
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONObject(java.util.Map)
     */
    @Override
    public JSONObject convertToJSONObject(Map<String, ?> source) throws JSONConversionException {
        return convertToJSONObject(source, Optional.empty(), Optional.empty());
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONObject(java.util.Map, org.ghotibeaun.json.converters.valueconverter.ValueConverter)
     */
    @Override
    public JSONObject convertToJSONObject(Map<String, ?> source, ValueConverter<?> converter)
            throws JSONConversionException {
        return convertToJSONObject(source, Optional.of(converter), Optional.empty());
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONObject(java.util.Map, java.lang.Class)
     */
    @Override
    public JSONObject convertToJSONObject(Map<String, ?> source, Class<?> targetClass) throws JSONConversionException {
        return convertToJSONObject(source, Optional.empty(), Optional.of(targetClass));
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.AbstractClassConverter#convertToJSONObject(java.util.Map, java.util.Optional, java.util.Optional)
     */
    @Override
    public JSONObject convertToJSONObject(Map<String, ?> source, Optional<ValueConverter<?>> valueConverter,
            Optional<Class<?>> targetClass) throws JSONConversionException {
        // TODO Auto-generated method stub
        final JSONObject newObject = NodeFactory.newJSONObject();
        source.entrySet().forEach(entry -> processMapEntry(newObject, entry, valueConverter, targetClass));
        return newObject;
    }

    /**
     * Process {@link ScannerEntry} items to get values from a source class
     * @param scanner The {@link ClassScanner} instance containing the {@link ScannerEntry} values
     * @param source the source object to convert
     * @param json The JSONObject instance to populate data into.
     * @throws JSONConversionException
     */
    private void processClass(ClassScanner scanner, Object source, JSONObject json) throws JSONConversionException {
        scanner.entries().forEach(entry -> processEntry(entry, source, json));
    }

    /**
     * Process a {@link ScannerEntry} and apply values to the JSONObject
     * @param entry the ScannerEntry
     * @param source the source object containing the data 
     * @param json the JSONObject instance to populate values into.
     * @throws JSONConversionException
     */
    private void processEntry(ScannerEntry entry, Object source, JSONObject json) throws JSONConversionException {
        final Optional<MemberHandler<?>> memberHandler = getMemberHandler(entry, source);

        if (memberHandler.isPresent()) {
            json.put(entry.getJsonKey(), memberHandler.get().getJSONValue(source));
        }
    }

    /**
     * Locate the Member (Field or Method) that will be used to extract the data from the source object
     * @param entry The {@link ScannerEntry} containing the references to the current value to populate
     * @param source The source object
     * @return an Optional MemberHandler. It may return an {@link Optional#isEmpty()} if {@link ScannerEntry#isIgnore()}
     * is set to true
     * @throws JSONConversionException
     */
    private Optional<MemberHandler<?>> getMemberHandler(ScannerEntry entry, Object source) throws JSONConversionException {
        MemberHandler<?> handler = null;

        if (!entry.isIgnore()) {
            if (entry.getContainerClass().equals(source.getClass())) {
                try {
                    final Field field = source.getClass().getDeclaredField(entry.getFieldName());
                    final Optional<ValueConverter<?>> valueConverter = AnnotationUtils.getJSONValueConverter(field);
                    handler = MemberHandler.newMemberHandler(field, entry, valueConverter);
                } catch (NoSuchFieldException | SecurityException e) {
                    throw new JSONConversionException("Error retrieving field [" + entry.getFieldName() + "] from class "
                            + entry.getContainerClass().getName() + "]", e);
                }
            } else {
                final Optional<Method> method = entry.getGetterMethod();
                if (method.isPresent()) {
                    final Optional<ValueConverter<?>> valueConverter = AnnotationUtils.getJSONValueConverter(method.get());
                    handler = MemberHandler.newMemberHandler(method.get(), entry, valueConverter);
                } else {
                    throw new JSONConversionException("Could not create getter method for " + entry.getJsonKey());
                }
            }
        }
        return Optional.ofNullable(handler);
    }

    /**
     * Process a list item
     * @param targetArray The JSONArray values will be added to
     * @param item the List item value
     * @param converter The Optional {@link ValueConverter}. If set, the item value will be converted first through the
     * {@link ValueConverter#convertValue(Object)} before setting the value into the JSONArray
     * @param targetClass The Optional {@link Class targetClass}. If set, the item must implement or inherit from the
     * item's class type
     * @throws JSONConversionException
     */
    private void processListItem(JSONArray targetArray, Object item, Optional<ValueConverter<?>> converter, Optional<Class<?>> targetClass) throws JSONConversionException {
        final JSONValue<?> value = convertValue(item, converter, targetClass);
        targetArray.add(value);
    }

    private void processMapEntry(JSONObject targetObject, Entry<String, ?> mapEntry, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException {
        final JSONValue<?> value = convertValue(mapEntry.getValue(), valueConverter, targetClass);
        targetObject.put(mapEntry.getKey(), value);
    }

    private JSONValue<?> createJSONValue(Object item, Optional<ValueConverter<?>> converter, Optional<Class<?>> targetClass) throws JSONConversionException {
        Optional<JSONValue<?>> jsonValue = null;

        if (item == null) {
            jsonValue = createValueFromObject(item);
        } else if (converter.isPresent()) {
            final Object val = converter.get().convertValue(item);
            jsonValue = createValueFromObject(val);
        } else if (targetClass.isPresent()) {
            if (item.getClass().isNestmateOf(targetClass.get())) {
                final JSONObject val = convertToJSONObject(targetClass.get().cast(item));
                jsonValue = createValueFromObject(val);
            } else {
                throw new JSONConversionException("The value cannot be cast into target class");
            }
        } else if (item instanceof Map) {
            @SuppressWarnings("unchecked")
            final
            JSONObject val = convertToJSONObject((Map<String, ?>)item);
            jsonValue = createValueFromObject(val);
        } else if (item instanceof List) {
            final JSONArray val = convertToJSONArray((List<?>)item);
            jsonValue = createValueFromObject(val);
        } else {
            jsonValue = createValueFromObject(item);
        }

        if (jsonValue.isPresent()) {
            return jsonValue.get();
        } else {
            throw new JSONConversionException("Unable to convert value to JSONValue");
        }

    }

    private Optional<JSONValue<?>> createValueFromObject(Object val) throws JSONConversionException {
        JSONValue<?> jsonValue = null;

        if (val == null) {
            jsonValue = NodeFactory.newJSONNullValue();
        } else if (val instanceof String) {
            jsonValue = NodeFactory.newStringValue((String) val);
        } else if (val instanceof Number) {
            jsonValue = NodeFactory.newNumberValue((Number) val);
        } else if (val instanceof Boolean) {
            jsonValue = NodeFactory.newBooleanValue((Boolean) val);
        } else if (val instanceof NullObject) {
            jsonValue = NodeFactory.newJSONNullValue();
        } else if (val instanceof Map) {
            @SuppressWarnings("unchecked")
            final JSONObject obj = convertToJSONObject((Map<String, ?>) val);
            jsonValue = NodeFactory.newJSONObjectValue(obj);
        } else if (val instanceof List) {
            @SuppressWarnings("unchecked")
            final JSONArray arr = convertToJSONArray((List<Object>) val);
            jsonValue = NodeFactory.newJSONArrayValue(arr);
        } else if (val instanceof JSONObject) {
            jsonValue = NodeFactory.newJSONObjectValue((JSONObject)val);
        } else if (val instanceof JSONArray) {
            jsonValue = NodeFactory.newJSONArrayValue((JSONArray)val);
        } else {
            final JSONObject convert = convertToJSONObject(val);
            jsonValue = NodeFactory.newJSONObjectValue(convert);
        }

        return Optional.ofNullable(jsonValue);
    }



}
