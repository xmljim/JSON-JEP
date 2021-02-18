package org.ghotibeaun.json;

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


}
