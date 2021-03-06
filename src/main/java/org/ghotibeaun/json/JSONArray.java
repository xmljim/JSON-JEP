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

import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;
import org.ghotibeaun.json.merge.MergeProcessor;
import org.ghotibeaun.json.merge.MergeResult;
import org.ghotibeaun.json.merge.strategies.ArrayConflict;
import org.ghotibeaun.json.merge.strategies.ObjectConflict;

/**
 * Interface holding an array of {@linkplain JSONValue} objects
 * @author jearley
 *
 */
public interface JSONArray extends JSONListNode {

    /**
     * Add a String value to the JSONArray
     * @param string the String value.
     */
    void add(String string);

    /**
     * Add a boolean value
     * @param bool the boolean value
     */
    void add(Boolean bool);

    /**
     * Add a number value
     * @param number the number value
     */
    void add(Number number);

    /**
     * Add a JSONArray value
     * @param array a JSONArrray
     */
    void add(JSONArray array);

    /**
     * Add a JSONObject value
     * @param obj the JSONObject
     */
    void add(JSONObject obj);

    /**
     * Add a JSONNode instance. This will be converted to either a JSONArray or JSONObject on inspection
     * @param node the JSONNode
     */
    default void add(JSONNode node) {
        if (node.isArray()) {
            add(node.asJSONArray());
        } else {
            add(node.asJSONObject());
        }
    }

    /**
     * Get a String value
     * @param index the JSONArray index position
     * @return the String value of the array at that index
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as String
     */
    String getString(int index) throws JSONInvalidValueTypeException;

    /**
     * Return a Number value
     * @param index the JSONArray index position
     * @return A number
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as Number
     */
    Number getNumber(int index) throws JSONInvalidValueTypeException;

    /**
     * Return a long value
     * @param index the JSONArray index position
     * @return a long value
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as long
     */
    long getLong(int index) throws JSONInvalidValueTypeException;

    /**
     * Return a integer value
     * @param index the JSONArray index position
     * @return a integer value
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as integer
     */
    int getInt(int index) throws JSONInvalidValueTypeException;

    /**
     * Return a boolean value
     * @param index the JSONArray index position
     * @return a boolean value
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as long
     */
    boolean getBoolean(int index) throws JSONInvalidValueTypeException;

    /**
     * Return a JSONArray value
     * @param index the JSONArray index position
     * @return a JSONArray value
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as JSONArray
     */
    JSONArray getJSONArray(int index) throws JSONInvalidValueTypeException;

    /**
     * Return a JSONOBject value
     * @param index the JSONArray index position
     * @return a JSONObject value
     * @throws JSONInvalidValueTypeException thrown if value cannot be returned as JSONObject
     */
    JSONObject getJSONObject(int index) throws JSONInvalidValueTypeException;

    /**
     * Return the value type for value at the specified index
     * @param index the JSONArray index position
     * @return the value type enum value
     */
    JSONValueType getValueType(int index);

    /**
     * Return the value at the specified index
     * @param index the JSONArray index position
     * @return the casted value
     */
    @SuppressWarnings("unchecked")
    default <T> T getValue(int index) {
        return (T) get(index).getValue();
    }

    /**
     * Return the last value in the array
     * @param <T> The value type
     * @return the last value in the array, or null if the array is empty
     */
    @SuppressWarnings("unchecked")
    default <T> T getLastValue() {
        if (getLast() != null) {
            return (T) getLast().getValue();
        } else {
            return null;
        }
    }


    /**
     * Return the first value in the array
     * @param <T> the value type
     * @return the first value in the array, or null if the array is empty
     */
    @SuppressWarnings("unchecked")
    default <T> T getFirstValue() {
        if (getFirst() != null) {
            return (T) getFirst().getValue();
        } else {
            return null;
        }
    }

    /**
     * Merge another JSONArray into this instance.
     * 
     * <p>
     *   <strong>HERE THERE BE DRAGONS:</strong> Once the merge happens, the original Node value is replaced. Know your data
     * <p>
     * @param secondary the other JSONArray instance to merge
     * @param arrayConflict the Array Conflict Strategy to use
     * @param objectConflict the Object Conflict Strategy to use
     */
    default void merge(JSONArray secondary, ArrayConflict arrayConflict, ObjectConflict objectConflict) {
        MergeProcessor.merge(this, secondary, arrayConflict, objectConflict, MergeResult.MERGE_PRIMARY);
    }

    /**
     * Merge the current JSONArray instance with another and return the merged instance. The original instance 
     * is left in its original state. 
     * @param secondary the other JSONArray instance to merge
     * @param arrayConflict the Array Conflict Strategy to use
     * @param objectConflict the Object Conflict Strategy to use
     * @return
     */
    default JSONArray mergeToCopy(JSONArray secondary, ArrayConflict arrayConflict, ObjectConflict objectConflict) {
        return MergeProcessor.merge(this, secondary, arrayConflict, objectConflict, MergeResult.NEW_INSTANCE);
    }
}
