package org.ghotibeaun.json.converters.valueconverter;

import java.util.Optional;

import org.ghotibeaun.json.exception.JSONConversionException;

public interface ValueConverter<T> {

    /**
     * Specifies the list of value types this converter will accept. An empty list indicates
     * this converter will accept ALL types
     * @return a list of class types
     */
    public Class<?>[] accepts();

    public String[] getArgs();

    <V> T convertValue(V value) throws JSONConversionException;

    static Optional<ValueConverter<?>> empty() {
        return Optional.empty();
    }

}
