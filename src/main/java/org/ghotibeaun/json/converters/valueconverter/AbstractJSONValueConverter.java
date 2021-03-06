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

/**
 * Abstract implementation for the {@link ValueConverter} interface.
 * Contains utility methods to validate the incoming value type before converting the value
 * @author Jim Earley (xml.jim@gmail.com)
 *
 * @param <T>
 */
public abstract class AbstractJSONValueConverter<T> implements ValueConverter<T> {
    private final String[] args;

    /**
     * Constructor
     * @param args vararg array of argument strings
     */
    public AbstractJSONValueConverter(String...args) {
        this.args = args;
    }

    @Override
    public abstract Class<?>[] accepts();

    /**
     * Converts the {@link #accepts()} class array to a printable string
     * @return
     */
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

    /**
     * Return a formatted invalid type message
     * @param <V> the value type
     * @param value the value
     * @return the formatted invalid message
     */
    public <V> String getInvalidMessage(V value) {
        return "INVALID VALUE TYPE CONVERSION: Current value type {" 
                + value.getClass().getName() + "} is not supported by this converter. This converter accepts: "
                + getAcceptedClassList();
    }

    /**
     * Returns the arguments passed into the concrete ValueConverter instance
     */
    @Override
    public String[] getArgs() {
        return args;
    }

    /**
     * Return the indexed argument
     * @param index the index position
     * @return the value at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index value does not exist
     */
    public String getArg(int index) {
        return getArgs()[index];
    }

    /**
     * Evaluates the value type against the ValueConverter's accepted value types
     * @param <V> the value type
     * @param value the value
     * @return true if the value type matches one of types in the {@link #accepts()} array; false otherwise
     */
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

    /**
     * perform the value conversion and return the result
     * @param <V> the value type
     * @param value the value
     * @return the converted value
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    public abstract <V> T getConvertedValue(V value) throws JSONConversionException;

}
