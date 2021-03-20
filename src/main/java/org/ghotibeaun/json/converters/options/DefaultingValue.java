package org.ghotibeaun.json.converters.options;

import java.util.List;
import java.util.Optional;

public abstract class DefaultingValue<T> {
    private final T value;

    protected DefaultingValue(T value) {
        this.value = value;
    }

    public static <T> DefaultingValue<T> of(T defaultValue, Optional<T> setValue) {
        return new SingleDefaultingValue<>(setValue.orElse(defaultValue));
    }

    @SuppressWarnings("unchecked")
    public static <T> DefaultingValue<T> ofList(List<T> defaultList, List<T> values) {
        final List<T> setValues = values.size() != 0 ? values : defaultList;
        return (DefaultingValue<T>) new MultiDefaultingValue<>(setValues);
    }

    public T getValue() {
        return value;
    }

}
