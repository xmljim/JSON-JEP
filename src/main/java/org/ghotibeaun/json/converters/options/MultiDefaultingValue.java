package org.ghotibeaun.json.converters.options;

import java.util.List;

public class MultiDefaultingValue<T> extends DefaultingValue<List<T>> {
    public MultiDefaultingValue(List<T> value) {
        super(value);
    }

    @Override
    public List<T> getValue() {
        return super.getValue();
    }

}
