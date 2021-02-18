package org.ghotibeaun.json.converters.options;

import org.ghotibeaun.json.converters.valueconverter.StringJSONValueConverter;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;

public enum ValueConverterType {
    STRING(StringJSONValueConverter.class);

    private Class<? extends ValueConverter<?>> clazz;

    public Class<? extends ValueConverter<?>> getConverterClass() {
        return clazz;
    }

    private ValueConverterType(Class<? extends ValueConverter<?>> clazz) {
        this.clazz = clazz;
    }
}
