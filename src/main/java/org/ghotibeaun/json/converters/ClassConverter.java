package org.ghotibeaun.json.converters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

/**
 * Converts Java class instances to JSON
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public interface ClassConverter extends Converter {

    /**
     * Convert an object to a JSONNode
     * @param <T> The source type
     * @param source the object instance to convert
     * @return a JSONNode. Use {@link JSONNode#isArray()} or {@link JSONNode#isObject()} to determine how to cast.
     * The use {@link JSONNode#asJSONArray()} or {@link JSONNode#asJSONObject()} to cast and access the values of the
     * JSONNode
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    <T> JSONNode convertToJSON(T source) throws JSONConversionException;

    /**
     * Convert to a JSONObject
     * @param <T> The source type
     * @param source the object instance
     * @return A JSONObject representing the object
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    <T> JSONObject convertToJSONObject(T source) throws JSONConversionException;

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings.
     * @param source The Map to convert
     * @return A JSONObject. The Map's keys become the JSONObject keys. Values are converted to JSONValue instances based on type
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    JSONObject convertToJSONObject(Map<String, ?> source) throws JSONConversionException;

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings. Map Entry values are passed through a ValueConverter
     * prior to setting the value
     * @param source The Map to convert
     * @param converter a ValueConverter to be applied to each entry on the map.
     * @return A JSONObject. The Map's keys become the JSONObject keys. Values are converted to JSONValue instances using the 
     * specified ValueConverter
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    JSONObject convertToJSONObject(Map<String, ?> source, ValueConverter<?> converter) throws JSONConversionException;

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings. Map values are converted to instances of the targetClass
     * @param source The Map to convert
     * @param targetClass The target class of each entry to be used for scanning and extracting values. The targetClass
     * must be concrete and implement or inherit from the value's class instance.
     * @return a JSONObject. The Map's keys become the JSONObject keys. Values are converted from instances of the targetClass
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    JSONObject convertToJSONObject(Map<String, ?> source, Class<?> targetClass) throws JSONConversionException;

    /**
     * Convert a Map to JSONObject. Optionally uses a {@link ValueConverter} or {@link Class targetClass} to
     * set the values of each entry. <strong>Note: You can use either a ValueConverter <em>OR</em> targetClass,
     * but not <em>both</em>. Otherwise, a {@link JSONConversionException} will be thrown</strong>
     * @param source The map to convert
     * @param valueConverter The optional ValueConverter. If a ValueConverter is not being used, apply {@link Optional#empty()}
     * @param targetClass The optional target class. If a target class is not used, apply {@link Optional#empty()}
     * @return a JSONObject The Map's keys become the JSONObject keys. Values are set based on the presence of 
     * a ValueConverter, target class, or neither.
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    JSONObject convertToJSONObject(Map<String, ?> source, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;

    /**
     * Convert a list to a JSONArray
     * @param source the List to convert
     * @return A JSONArray
     * @throws JSONConversionException thrown if a conversion error occurs
     */
    JSONArray convertToJSONArray(List<?> source) throws JSONConversionException;


    JSONArray convertToJSONArray(List<?> source, Class<?> targetClass) throws JSONConversionException;

    JSONArray convertToJSONArray(List<?> source, ValueConverter<?> valueConverter) throws JSONConversionException;

    JSONArray convertToJSONArray(List<?> source, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;

    JSONValue<?> convertValue(Object value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass) throws JSONConversionException;
}
