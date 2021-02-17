/*
 *
 * Copyright (c) 2018-2021, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.converters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.FactorySettings;

/**
 * Base class for ClassConverters
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public abstract class AbstractClassConverter extends AbstractConverter implements ClassConverter {

    /**
     * Returns the ClassConverter implementation. The implementation class is specified using the 
     * {@link FactorySettings#JSON_CLASS_CONVERTER_CLASS} property
     * @param option Any converter options to set on the Converter
     * @return the ClassConverter instance
     */
    public static ClassConverter getClassConverter(Options...option) {
        //return new ClassConverterImpl(option);
        final Optional<Class<?>> converterClass = FactorySettings.getFactoryClass(FactorySettings.JSON_CLASS_CONVERTER_CLASS);
        if (converterClass.isPresent()) {
            return (ClassConverter)ClassUtils.createConverter(converterClass.get(), option);
        } else {
            throw new JSONConversionException("No Class Converter class found");
        }
    }
    /**
     * Constructor
     */
    public AbstractClassConverter(Options... option) {
        super(option);
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
