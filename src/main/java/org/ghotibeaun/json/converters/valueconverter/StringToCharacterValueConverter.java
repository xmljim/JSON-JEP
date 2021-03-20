package org.ghotibeaun.json.converters.valueconverter;

import org.ghotibeaun.json.exception.JSONConversionException;

public class StringToCharacterValueConverter extends AbstractJSONValueConverter<Character> {

    @Override
    public Class<?>[] accepts() {
        return new Class[] {String.class};
    }

    @Override
    public <V> Character getConvertedValue(V value) throws JSONConversionException {
        return ((String)value).charAt(0);
    }

}
