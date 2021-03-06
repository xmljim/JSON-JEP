package org.ghotibeaun.json.converters.options;

import java.util.function.Function;

public interface ConverterOption<T> {
    OptionKey getKey();

    T getValue();

    <R> R eval(Function<T, R> function);

    <V> boolean matchesValue(V value);
}
