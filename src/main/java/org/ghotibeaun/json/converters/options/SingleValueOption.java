package org.ghotibeaun.json.converters.options;

public class SingleValueOption<T> implements Option<T> {
    private T value;

    public SingleValueOption(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

}
