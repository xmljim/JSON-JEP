package org.ghotibeaun.json.converters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.options.Options;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

/**
 * Utility entry point to access converter methods statically.
 * @author Jim Earley (jim.earley@fdiinc.com)
 *
 */
public final class Converters {

    /**
     * Convert a JSONObject into a Class instance
     * @param <T> the Class type.
     * @param targetClass the concrete class to instantiate. It must implement or inherit from the Class type.
     * @param json The JSONObject instance containing the data to initialize into the class instance
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return the specified Class instance
     * @throws JSONConversionException thrown when an error occurs during the conversion
     * @see JSONConverter#convertToClass(Class, JSONObject)
     */
    public static <T> T convertToClass(Class<T> targetClass, JSONObject jsonObject, Options... options) throws JSONConversionException {
        return AbstractJSONConverter.getJSONConverter(options).convertToClass(targetClass, jsonObject);
    }

    /**
     * Convert a JSONArray into a List of values
     * @param <T> The Class Type.
     * @param targetItemClass The concrete class for each item in the list.
     * @param array the JSONArray instance containing the data to initialize the list
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return a List of values
     * @throws JSONConversionException thrown when an error occurs during conversion
     * @see JSONConverter#convertToList(Class, JSONArray, Optional)
     */
    @Deprecated
    public static <T> List<T> convertToList(Class<T> targetClass, JSONArray jsonArray, Optional<ValueConverter<?>> valueConverter, Options...options) throws JSONConversionException {
        return AbstractJSONConverter.getJSONConverter(options).convertToList(targetClass, jsonArray, valueConverter);
    }

    public static <T> List<T> convertToList(JSONArray array, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass, Options...options) throws JSONConversionException {
        return AbstractJSONConverter.getJSONConverter(options).convertToList(array, valueConverter, targetClass);
    }

    public static <T> T convertValue(JSONValue<?> value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass, Options...options) throws JSONConversionException {
        return AbstractJSONConverter.getJSONConverter(options).convertValue(value, valueConverter, targetClass);
    }



    /**
     * Convert an object to a JSONNode
     * @param <T> The source type
     * @param source the object instance to convert
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return a JSONNode. Use {@link JSONNode#isArray()} or {@link JSONNode#isObject()} to determine how to cast.
     * The use {@link JSONNode#asJSONArray()} or {@link JSONNode#asJSONObject()} to cast and access the values of the
     * JSONNode
     * @throws JSONConversionException thrown if a conversion error occurs
     * @see ClassConverter#convertToJSON(Object)
     */
    public static <T> JSONNode convertToJSON(T source, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSON(source);
    }

    /**
     * Convert to a JSONObject
     * @param <T> The source type
     * @param source the object instance
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return A JSONObject representing the object
     * @throws JSONConversionException thrown if a conversion error occurs
     * @see ClassConverter#convertToJSONObject(Object)
     */
    public static <T> JSONObject convertToJSONObject(T source, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONObject(source);
    }

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings.
     * @param source The Map to convert
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return A JSONObject. The Map's keys become the JSONObject keys. Values are converted to JSONValue instances based on type
     * @throws JSONConversionException thrown if a conversion error occurs
     * @see ClassConverter#convertToJSONObject(Map)
     */
    public static JSONObject convertToJSONObject(Map<String, ?> source, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONObject(source);
    }

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings.
     * @param source The Map to convert
     * @param converter the ValueConverter that will be applied to each element in the map
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return A JSONObject. The Map's keys become the JSONObject keys. Values are converted to JSONValue instances based on type
     * @throws JSONConversionException thrown if a conversion error occurs
     * @see ClassConverter#convertToJSONObject(Map, ValueConverter)
     */
    public static JSONObject convertToJSONObject(Map<String, ?> source, ValueConverter<?> converter, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONObject(source, converter);
    }

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings.
     * @param source The Map to convert
     * @param targetClass the targetClass to cast each element in the map
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return A JSONObject. The Map's keys become the JSONObject keys. Values are converted to JSONValue instances based on type
     * @throws JSONConversionException thrown if a conversion error occurs
     * @see ClassConverter#convertToJSONObject(Map, Class)
     */
    public static JSONObject convertToJSONObject(Map<String, ?> source, Class<?> targetClass, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONObject(source, targetClass);
    }

    /**
     * Convert a Map to a JSONObject. Map keys must be Strings.
     * @param source The Map to convert
     * @param valueConverter the ValueConverter
     * @param targetClass the targetClass to cast each element in the map
     * @param options specifies JSON Conversion options to apply. Experimental - not fully implemented
     * @return A JSONObject. The Map's keys become the JSONObject keys. Values are converted to JSONValue instances based on type
     * @throws JSONConversionException thrown if a conversion error occurs
     * @see ClassConverter#convertToJSONObject(Map, Optional, Optional)
     */
    public static JSONObject convertToJSONObject(Map<String, ?> source, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONObject(source, valueConverter, targetClass);
    }

    /**
     * @see ClassConverter#convertToJSONArray(List)
     */
    public static JSONArray convertToJSONArray(List<?> source, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONArray(source);
    }

    /**
     * @see ClassConverter#convertToJSONArray(List, Class)
     */
    public static JSONArray convertToJSONArray(List<?> source, Class<?> targetClass, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONArray(source, targetClass);
    }

    /**
     * @see ClassConverter#convertToJSONArray(List, ValueConverter)
     */
    public static JSONArray convertToJSONArray(List<?> source, ValueConverter<?> valueConverter, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONArray(source, valueConverter);
    }

    /**
     * @see ClassConverter#convertToJSONArray(List, Optional, Optional)
     */
    public static JSONArray convertToJSONArray(List<?> source, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertToJSONArray(source, valueConverter, targetClass);
    }

    /**
     * @see ClassConverter#convertValue(Object, Optional, Optional)
     */
    public static JSONValue<?> convertValue(Object value, Optional<ValueConverter<?>> valueConverter, Optional<Class<?>> targetClass, Options...options) throws JSONConversionException {
        return AbstractClassConverter.getClassConverter(options).convertValue(value, valueConverter, targetClass);
    }

    private Converters() {
        //private to prevent instantiation;
    }
}
