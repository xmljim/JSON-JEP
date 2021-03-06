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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.options.ConverterOption;
import org.ghotibeaun.json.converters.options.OptionsBuilder;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

/**
 * Base class for ClassConverters
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public abstract class AbstractClassConverter extends AbstractConverter implements ClassConverter {
    /**
     * Constructor
     */
    public AbstractClassConverter(ConverterOption<?>... option) {
        super(option.length == 0 ? OptionsBuilder.defaultOptions() : option);
    }

    /**
     * Returns the ClassConverter implementation. The implementation class is specified using the 
     * {@link FactorySettings#JSON_CLASS_CONVERTER_CLASS} property
     * @param option Any converter options to set on the Converter
     * @return the ClassConverter instance
     */
    public static ClassConverter getClassConverter(ConverterOption<?>...option) {
        //return new ClassConverterImpl(option);
        final Optional<Class<?>> converterClass = FactorySettings.getFactoryClass(Setting.CLASS_CONVERTER_CLASS);
        if (converterClass.isPresent()) {
            return (ClassConverter)ClassUtils.createConverter(converterClass.get(), option);
        } else {
            throw new JSONConversionException("No Class Converter class found");
        }
    }


    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSON(java.lang.Object)
     */
    @Override
    public abstract <T> JSONNode convertToJSON(T source) throws JSONConversionException;

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSONObject(java.lang.Object)
     */
    @Override
    public abstract <T> JSONObject convertToJSONObject(T source) throws JSONConversionException; 

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSONArray(java.util.List)
     */
    @Override
    public abstract JSONArray convertToJSONArray(List<?> source) throws JSONConversionException;


    @Override
    public abstract JSONArray convertToJSONArray(List<?> source, Class<?> targetClass) throws JSONConversionException;

    @Override
    public abstract JSONArray convertToJSONArray(List<?> source, ValueConverter<?> valueConverter) throws JSONConversionException;

    @Override
    public abstract JSONArray convertToJSONArray(List<?> source, Optional<ValueConverter<?>> valueConverter, 
            Optional<Class<?>> targetClass) throws JSONConversionException;


    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertValue(java.lang.Object, java.util.Optional, java.util.Optional)
     */
    @Override
    public abstract JSONValue<?> convertValue(Object value, Optional<ValueConverter<?>> valueConverter,
            Optional<Class<?>> targetClass) throws JSONConversionException;
    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSONObject(java.util.Map)
     */
    @Override
    public abstract JSONObject convertToJSONObject(Map<String, ?> source) throws JSONConversionException;
    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSONObject(java.util.Map, org.ghotibeaun.json.converters.valueconverter.ValueConverter)
     */
    @Override
    public abstract JSONObject convertToJSONObject(Map<String, ?> source, ValueConverter<?> converter)
            throws JSONConversionException;
    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSONObject(java.util.Map, java.lang.Class)
     */
    @Override
    public abstract JSONObject convertToJSONObject(Map<String, ?> source, Class<?> targetClass) throws JSONConversionException;
    /* (non-Javadoc)
     * @see org.ghotibeaun.json.converters.ClassConverter#convertToJSONObject(java.util.Map, java.util.Optional, java.util.Optional)
     */
    @Override
    public abstract JSONObject convertToJSONObject(Map<String, ?> source, Optional<ValueConverter<?>> valueConverter,
            Optional<Class<?>> targetClass) throws JSONConversionException;


}
