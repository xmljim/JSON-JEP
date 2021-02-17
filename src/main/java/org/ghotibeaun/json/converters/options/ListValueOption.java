package org.ghotibeaun.json.converters.options;

import java.util.Arrays;
import java.util.List;

public class ListValueOption<T> implements Option<List<T>> {
    private List<T> values;

    @SafeVarargs
    public ListValueOption(T... values) {
        this.values = Arrays.asList(values);
    }

    @Override
    public void setValue(List<T> value) {
        this.values = value;        
    }

    @Override
    public List<T> getValue() {
        // TODO Auto-generated method stub
        return this.values;
    }



}
