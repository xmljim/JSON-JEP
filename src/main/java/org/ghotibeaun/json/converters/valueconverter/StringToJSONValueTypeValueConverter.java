package org.ghotibeaun.json.converters.valueconverter;

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.exception.JSONConversionException;

public class StringToJSONValueTypeValueConverter extends AbstractJSONValueConverter<JSONValueType> {

    @Override
    public Class<?>[] accepts() {
        return new Class[] {String.class};
    }

    @Override
    public <V> JSONValueType getConvertedValue(V value) throws JSONConversionException {
        return JSONValueType.valueOf(((String)value).toUpperCase());
    }

}
