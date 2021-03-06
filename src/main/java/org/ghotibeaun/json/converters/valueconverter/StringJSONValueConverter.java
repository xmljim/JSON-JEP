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

import org.ghotibeaun.json.exception.JSONConversionException;

/**
 * Converts a JSONValue to a String. Optionally accepts a single argument to format the value
 * <p><strong>NOTE:</strong> This class uses the native {@link String#format(String, Object...)} method. 
 * As a result, please consult the appropriate formatting tokens.
 * </p>
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class StringJSONValueConverter extends AbstractJSONValueConverter<String> {

    public StringJSONValueConverter(String... args) {
        super(args);
    }

    /**
     * Accepts any type
     */
    @Override
    public Class<?>[] accepts() {
        return new Class<?>[] {};
    }

    @Override
    public <V> String getConvertedValue(V value) throws JSONConversionException {
        if (getArgs().length == 0) {
            return value.toString();
        } else {
            System.out.println(value);
            //MessageFormat format = new MessageFormat(getArg(0));
            final String fmt = String.format(getArg(0), value.toString());
            System.out.println("StringJSONValueConverter: " + fmt);
            return fmt;
        }
    }

}
