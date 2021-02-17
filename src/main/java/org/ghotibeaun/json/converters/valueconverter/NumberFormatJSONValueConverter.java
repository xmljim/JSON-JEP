package org.ghotibeaun.json.converters.valueconverter;

import java.text.DecimalFormat;

import org.ghotibeaun.json.exception.JSONConversionException;

public class NumberFormatJSONValueConverter extends AbstractJSONValueConverter<String> {

    public NumberFormatJSONValueConverter(String... args) {
        super(args);
    }

    @Override
    public Class<?>[] accepts() {
        return new Class<?>[] {long.class, 
            Long.class, 
            double.class, 
            Double.class, 
            float.class, 
            Float.class,
            int.class,
            Integer.class};
    }

    @Override
    public <V> String getConvertedValue(V value) throws JSONConversionException {
        if (!accept(value)) {
            throw new JSONConversionException(getInvalidMessage(value));
        }

        final DecimalFormat format = new DecimalFormat(getArg(0));
        String convertValue = null;

        try {
            convertValue = format.format(value);
        } catch (final Exception e) {
            throw new JSONConversionException(e);
        }

        return convertValue;
    }
}