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
package org.ghotibeaun.json.converters.valueconverter;

import java.text.DecimalFormat;

import org.ghotibeaun.json.exception.JSONConversionException;

public class NumberFormatJSONValueConverter extends AbstractJSONValueConverter<String> {

    public NumberFormatJSONValueConverter(String... args) {
        super(args);
    }

    @Override
    public Class<?>[] accepts() {
        return new Class<?>[] {long.class, 
            Long.class, 
            double.class, 
            Double.class, 
            float.class, 
            Float.class,
            int.class,
            Integer.class};
    }

    @Override
    public <V> String getConvertedValue(V value) throws JSONConversionException {
        if (!accept(value)) {
            throw new JSONConversionException(getInvalidMessage(value));
        }

        final DecimalFormat format = new DecimalFormat(getArg(0));
        String convertValue = null;

        try {
            convertValue = format.format(value);
        } catch (final Exception e) {
            throw new JSONConversionException(e);
        }

        return convertValue;
    }
}