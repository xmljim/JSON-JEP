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
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

public abstract class AbstractJSONConverter extends AbstractConverter implements JSONConverter {

    public AbstractJSONConverter(Options... option) {
        super(option);
    }

    public static JSONConverter getJSONConverter(Options... options) {
        //return new ClassConverterImpl(option);
        final Optional<Class<?>> converterClass = FactorySettings.getFactoryClass(Setting.JSON_CONVERTER_CLASS);
        if (converterClass.isPresent()) {
            return (JSONConverter)ClassUtils.createConverter(converterClass.get(), options);
        } else {
            throw new JSONConversionException("No Class Converter class found");
        }
    }


    @Override
    public abstract <T> T convertToClass(Class<T> targetClass, JSONObject json) throws JSONConversionException;

    @Override
    @Deprecated
    public abstract <T> List<T> convertToList(Class<T> targetItemClass, JSONArray array, Optional<ValueConverter<?>> valueConverter) throws JSONConversionException;

    @Override
    public abstract <T> List<T> convertToList(JSONArray array, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;

    @Override
    public abstract <T> T convertValue(JSONValue<?> value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;
}
