package org.ghotibeaun.json.converters.options;

import java.util.List;

public class MultivalueConverterOption<T> extends AbstractConverterOption<List<T>> {

    public MultivalueConverterOption(OptionKey key, DefaultingValue<List<T>> defaultingValue) {
        super(key, defaultingValue);
        // TODO Auto-generated constructor stub
    }


    @SuppressWarnings("unlikely-arg-type")
    @Override
    public <V> boolean matchesValue(V value) {
        // TODO Auto-generated method stub
        return getValue().contains(value);
    }

}
