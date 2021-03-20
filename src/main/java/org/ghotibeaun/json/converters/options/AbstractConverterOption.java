package org.ghotibeaun.json.converters.options;

import java.util.function.Function;

public abstract class AbstractConverterOption<T> implements ConverterOption<T> {
    private final OptionKey key;
    private final T value;


    public AbstractConverterOption(OptionKey key, DefaultingValue<T> defaultingValue) {
        this.key = key;
        this.value = defaultingValue.getValue();
    }

    @Override
    public OptionKey getKey() {
        return key;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public <R> R eval(Function<T, R> function) {
        return function.apply(getValue());
    }

    @Override
    public abstract <V> boolean matchesValue(V value);

}
