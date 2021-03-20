/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
package org.ghotibeaun.json;

import java.io.Serializable;

/**
 * Interface for values stored in {@linkplain JSONObject} or {@linkplain JSONArray} containers
 * @author jearley
 *
 * @param <T> the value type
 */
public interface JSONValue<T> extends Serializable, Comparable<JSONValue<T>> {
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

    /**
     * Pretty print using a specified indent value
     * @param indent the indent value
     * @return a pretty-printed JSON structure
     */
    String prettyPrint(int indent);

    boolean isEquivalent(JSONValue<?> otherValue);


}
