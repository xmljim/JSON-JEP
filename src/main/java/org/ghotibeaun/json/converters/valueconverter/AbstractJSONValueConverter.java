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

import java.util.Arrays;
import java.util.Optional;

import org.ghotibeaun.json.exception.JSONConversionException;

public abstract class AbstractJSONValueConverter<T> implements ValueConverter<T> {
    private final String[] args;

    public AbstractJSONValueConverter(String...args) {
        this.args = args;
    }

    @Override
    public abstract Class<?>[] accepts();

    public String getAcceptedClassList() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (final Class<?> c : accepts()) {
            builder.append(c.getName());
            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    public <V> String getInvalidMessage(V value) {
        return "INVALID VALUE TYPE CONVERSION: Current value type {" 
                + value.getClass().getName() + "} is not supported by this converter. This converter accepts: "
                + getAcceptedClassList();
    }

    @Override
    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return getArgs()[index];
    }

    public <V> boolean accept(V value) {
        if (accepts().length == 0) {
            return true;
        } else {
            final Optional<Class<?>> optional = Arrays.stream(accepts())
                    .filter(clazz -> clazz.equals(value.getClass())).findFirst();

            return optional.isPresent();
        }
    }

    @Override
    public <V> T convertValue(V value) throws JSONConversionException {
        if (accept(value)) {
            return getConvertedValue(value);
        } else {
            throw new JSONConversionException(getInvalidMessage(value));
        }
    }

    public abstract <V> T getConvertedValue(V value) throws JSONConversionException;

}
