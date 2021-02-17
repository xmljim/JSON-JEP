package org.ghotibeaun.json.converters;

import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public abstract class AbstractJSONConverter extends AbstractConverter implements JSONConverter {

    public AbstractJSONConverter(Options... option) {
        super(option);
    }

    public static JSONConverter getJSONConverter(Options... options) {
        //return new JSONConverterImpl(options);
        return new JSONConverterImpl(options);
    }


    @Override
    public abstract <T> T convertToClass(Class<T> targetClass, JSONObject json) throws JSONConversionException;

    @Override
    public abstract <T> List<T> convertToList(Class<T> targetItemClass, JSONArray array, Optional<ValueConverter<?>> valueConverter) throws JSONConversionException;

}
