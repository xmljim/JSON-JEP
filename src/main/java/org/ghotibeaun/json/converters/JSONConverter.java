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
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public interface JSONConverter extends Converter {

    /**
     * Convert a JSONObject into a Class instance
     * @param <T> the Class type.
     * @param targetClass the concrete class to instantiate. It must implement or inherit from the Class type.
     * @param json The JSONObject instance containing the data to initialize into the class instance
     * @return the specified Class instance
     * @throws JSONConversionException thrown when an error occurs during the conversion
     */
    <T> T convertToClass(Class<T> targetClass, JSONObject json) throws JSONConversionException;

    /**
     * Convert a JSONArray into a List of values
     * @param <T> The Class Type.
     * @param targetItemClass The concrete class for each item in the list.
     * @param array the JSONArray instance containing the data to initialize the list
     * @return a List of values
     * @throws JSONConversionException thrown when an error occurs during conversion
     */
    @Deprecated
    <T> List<T> convertToList(Class<T> targetItemClass, JSONArray array, Optional<ValueConverter<?>> valueConverter) throws JSONConversionException;


    <T> List<T> convertToList(JSONArray array, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;

    <T> T convertValue(JSONValue<?> value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;

}
