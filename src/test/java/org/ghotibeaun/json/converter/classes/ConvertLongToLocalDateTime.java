package org.ghotibeaun.json.converter.classes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.ghotibeaun.json.converters.valueconverter.AbstractJSONValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public class ConvertLongToLocalDateTime extends AbstractJSONValueConverter<LocalDateTime> {

    public ConvertLongToLocalDateTime(String...args) {
        super(args);
    }

    @Override
    public Class<?>[] accepts() {
        return new Class[]{Long.class, long.class};
    }

    @Override
    public <V> LocalDateTime getConvertedValue(V value) throws JSONConversionException {
        final LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long)value), ZoneId.systemDefault());
        return ldt;

    }

}
