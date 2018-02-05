package org.ghotibeaun.json;

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

    void putNull(String key);

    JSONObject getJSONObject(String key);
    JSONArray getJSONArray(String key);

    Number getNumber(String key);

    long getLong(String key);

    int getInt(String key);

    boolean getBoolean(String key);

    JSONValueType getValueType(String key);

    String getString(String key);

}
