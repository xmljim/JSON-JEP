package org.ghotibeaun.json.converter.classes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.ghotibeaun.json.converters.valueconverter.AbstractJSONValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public class ConvertLocalDateTimeToLong extends AbstractJSONValueConverter<Long> {

    @Override
    public Class<?>[] accepts() {
        return new Class[] {LocalDateTime.class};
    }

    @Override
    public <V> Long getConvertedValue(V value) throws JSONConversionException {
        //This is inane.
        final ZoneOffset stoopid = ZoneId.systemDefault().getRules().getOffset((LocalDateTime)value);

        return ((LocalDateTime)value).toInstant(stoopid).toEpochMilli();
    }

}
