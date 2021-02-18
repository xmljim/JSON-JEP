package org.ghotibeaun.json.converters;

import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

public abstract class AbstractJSONConverter extends AbstractConverter implements JSONConverter {

    public AbstractJSONConverter(Options... option) {
        super(option);
    }

    public static JSONConverter getJSONConverter(Options... options) {
        //return new ClassConverterImpl(option);
        final Optional<Class<?>> converterClass = FactorySettings.getFactoryClass(Setting.JSON_CONVERTER_CLASS);
        if (converterClass.isPresent()) {
            return (JSONConverter)ClassUtils.createConverter(converterClass.get(), options);
        } else {
            throw new JSONConversionException("No Class Converter class found");
        }
    }


    @Override
    public abstract <T> T convertToClass(Class<T> targetClass, JSONObject json) throws JSONConversionException;

    @Override
    @Deprecated
    public abstract <T> List<T> convertToList(Class<T> targetItemClass, JSONArray array, Optional<ValueConverter<?>> valueConverter) throws JSONConversionException;

    @Override
    public abstract <T> List<T> convertToList(JSONArray array, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;

    @Override
    public abstract <T> T convertValue(JSONValue<?> value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;
}
