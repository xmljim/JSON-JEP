package org.ghotibeaun.json.converters.options;

public class SingleConverterOption<T> extends AbstractConverterOption<T> {

    public SingleConverterOption(OptionKey key, DefaultingValue<T> defaultingValue) {
        super(key, defaultingValue);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public <V> boolean matchesValue(V value) {
        // TODO Auto-generated method stub
        return value.equals(getValue());
    }

}
