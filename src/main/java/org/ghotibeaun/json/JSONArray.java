package org.ghotibeaun.json;

import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

public interface JSONArray extends JSONListNode {

    /**
     * Add a String value to the JSONArray
     * @param string the String value.
     */
    void add(String string);

    void add(Boolean bool);

    void add(Number number);

    void add(JSONArray array);

    void add(JSONObject obj);

    default void add(JSONNode node) {
        if (node.isArray()) {
            add(node.asJSONArray());
        } else {
            add(node.asJSONObject());
        }
    }

    String getString(int index) throws JSONInvalidValueTypeException;

    Number getNumber(int index) throws JSONInvalidValueTypeException;

    long getLong(int index) throws JSONInvalidValueTypeException;

    int getInt(int index) throws JSONInvalidValueTypeException;

    boolean getBoolean(int index) throws JSONInvalidValueTypeException;

    JSONArray getJSONArray(int index) throws JSONInvalidValueTypeException;

    JSONObject getJSONObject(int index) throws JSONInvalidValueTypeException;

    JSONValueType getValueType(int index);


}
