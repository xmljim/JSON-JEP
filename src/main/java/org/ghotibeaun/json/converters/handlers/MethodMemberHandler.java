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
