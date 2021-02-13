package org.ghotibeaun.json;

import java.io.Serializable;

/**
 * Interface for values stored in {@linkplain JSONObject} or {@linkplain JSONArray} containers
 * @author jearley
 *
 * @param <T> the value type
 */
public interface JSONValue<T> extends Serializable {
    /**
     * Return the value
     * @return the value
     */
    T getValue();

    @Override
    String toString();

    /**
     * Returns the value type hint
     * @return the value type hint
     */
    JSONValueType getType();

    /**
     * Returns whether the value is a primitive data type (String, Number, Boolean, Null)
     * @return true if the value is a primitive data type
     */
    boolean isPrimitive();

    /**
     * Returns whether the value is a {@linkplain JSONArray} instance
     * @return true if the value is a JSONArray
     */
    boolean isArray();

    /**
     * Returns whether the value is a {@link JSONObject} instance
     * @return true if the value is a JSONObject
     */
    default boolean isObject() {
        return !isPrimitive() && !isArray();
    }

    /**
     * Set the value
     * @param value the value
     */
    void setValue(T value);

    /**
     * Returns a pretty-printed JSON structure 
     * @return a pretty-printed JSON structure
     */
    String prettyPrint();

    String prettyPrint(int indent);


}
