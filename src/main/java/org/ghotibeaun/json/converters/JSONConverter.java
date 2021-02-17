package org.ghotibeaun.json.converters;

import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public interface JSONConverter extends Converter {

    /**
     * Convert a JSONObject into a Class instance
     * @param <T> the Class type.
     * @param targetClass the concrete class to instantiate. It must implement or inherit from the Class type.
     * @param json The JSONObject instance containing the data to initialize into the class instance
     * @return the specified Class instance
     * @throws JSONConversionException thrown when an error occurs during the conversion
     */
    <T> T convertToClass(Class<T> targetClass, JSONObject json) throws JSONConversionException;

    /**
     * Convert a JSONArray into a List of values
     * @param <T> The Class Type.
     * @param targetItemClass The concrete class for each item in the list.
     * @param array the JSONArray instance containing the data to initialize the list
     * @return a List of values
     * @throws JSONConversionException thrown when an error occurs during conversion
     */
    <T> List<T> convertToList(Class<T> targetItemClass, JSONArray array, Optional<ValueConverter<?>> valueConverter) throws JSONConversionException;


}
