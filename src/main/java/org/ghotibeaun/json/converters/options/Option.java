package org.ghotibeaun.json.converters.options;

public interface Option<T> {

    public T getValue();

    public void setValue(T value);
}
