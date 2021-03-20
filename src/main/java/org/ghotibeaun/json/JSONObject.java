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

import org.ghotibeaun.json.merge.MergeProcessor;
import org.ghotibeaun.json.merge.MergeResult;
import org.ghotibeaun.json.merge.strategies.ArrayConflict;
import org.ghotibeaun.json.merge.strategies.ObjectConflict;

/**
 * A JSON Object consisting of a collection of key-value pairs
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public interface JSONObject extends JSONMapNode {
    /**
     * Adds or replaces a number value
     *
     * @param key
     *            the key
     * @param n
     *            the number
     */
    void put(String key, Number n);

    /**
     * Adds or replaces a String value
     *
     * @param key
     *            the key
     * @param s
     *            the string value
     */
    void put(String key, String s);

    /**
     * Adds or replaces a boolean value
     *
     * @param key
     *            the key
     * @param b
     *            the boolean value
     */
    void put(String key, boolean b);

    /**
     * Set a JSONNode (either a JSONObject or JSONArray)
     * @param key the key
     * @param node the JSONNode
     */
    default void put(String key, JSONNode node) {
        if (node.isArray()) {
            put(key, node.asJSONArray());
        } else {
            put(key, node.asJSONObject());
        }
    }

    /**
     * Adds or replaces a {@linkplain JSONArray} value
     *
     * @param key
     *            the key
     * @param a
     *            the JSONArray
     */
    void put(String key, JSONArray a);

    /**
     * Adds or replaces a {@linkplain JSONObject} value
     *
     * @param key
     *            the key
     * @param o
     *            the JSONObject
     */
    void put(String key, JSONObject o);

    /**
     * Put a null value associated with a key 
     * @param key the key
     */
    void putNull(String key);

    /**
     * Return a JSONObject with the associated key
     * @param key the key
     * @return a JSONObject
     */
    JSONObject getJSONObject(String key);

    /**
     * Return a JSONArray 
     * @param key the key
     * @return a JSONArray
     */
    JSONArray getJSONArray(String key);

    /**
     * Return a Number. Value can be cast to the appropriate underlying type
     * @param key the key
     * @return a Number
     */
    Number getNumber(String key);

    /**
     * Return a long value
     * @param key the key
     * @return long value
     */
    long getLong(String key);

    /**
     * Return an integer
     * @param key the key
     * @return the integer value
     */
    int getInt(String key);

    /**
     * Return a boolean
     * @param key the key
     * @return the boolean value
     */
    boolean getBoolean(String key);

    /**
     * Returns the JSON value type
     * @param key the key
     * @return the value type
     */
    JSONValueType getValueType(String key);

    /**
     * Return a String
     * @param key the key
     * @return the String value
     */
    String getString(String key);

    /**
     * Return a casted value
     * @param <T> The cast type
     * @param key the key
     * @return the casted value
     */
    @SuppressWarnings("unchecked")
    default <T> T getValue(String key) {
        return (T) get(key).getValue();
    }

    /**
     * Merge another JSONObject into this instance.
     * 
     * <p>
     *   <strong>HERE THERE BE DRAGONS:</strong> Once the merge happens, the original Node value is replaced. Know your data
     * <p>
     * @param secondary the other JSONObject instance to merge
     * @param arrayConflict the Array Conflict Strategy to use
     * @param objectConflict the Object Conflict Strategy to use
     */
    default void merge(JSONObject secondary, ArrayConflict arrayConflict, ObjectConflict objectConflict) {
        MergeProcessor.merge(this, secondary, arrayConflict, objectConflict, MergeResult.MERGE_PRIMARY);
    }

    /**
     * Merge the current JSONObject instance with another and return the merged instance. The original instance 
     * is left in its original state. 
     * @param secondary the other JSONObject instance to merge
     * @param arrayConflict the Array Conflict Strategy to use
     * @param objectConflict the Object Conflict Strategy to use
     * @return
     */
    default JSONObject mergeToCopy(JSONObject secondary, ArrayConflict arrayConflict, ObjectConflict objectConflict) {
        return MergeProcessor.merge(this, secondary, arrayConflict, objectConflict, MergeResult.NEW_INSTANCE);
    }


}
