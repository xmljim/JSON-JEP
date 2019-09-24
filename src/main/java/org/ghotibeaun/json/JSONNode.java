package org.ghotibeaun.json;

import java.io.Serializable;

/**
 * Base interface for JSON entities
 * @author Jim Earley
 *
 */
public interface JSONNode extends Serializable {

    /**
     * Return as a JSON String
     * @return String representation of the JSONNode. Results are serialized as a non-formatted string
     */
    String toJSONString();

    /**
     * Pretty print the JSON as a string
     * @return pretty printed JSON string
     */
    String prettyPrint();

    /**
     * Pretty print JSON string with specified indents
     * @param indent the number of spaces to indent
     * @return the pretty printed JSON string
     */
    String prettyPrint(int indent);

    /**
     * Interrogate if the JSONNode is a JSONArray
     * @return true if it is a JSONArray; false otherwise
     */
    public default boolean isArray() {
        return (this instanceof JSONArray);
    }

    /**
     * Interrogate ifthe JSONNode is a JSONObject
     * @return true if it is a JSONObject; false otherwise
     */
    public default boolean isObject() {
        return (this instanceof JSONObject);
    }

    /**
     * Return the JSONNode as a {@linkplain JSONObject}
     * @return a JSONObject representation of the JSONNode
     */
    public default JSONObject asJSONObject() {
        return (JSONObject)this;
    }

    /**
     * Return the JSONNode as a {@linkplain JSONArray}
     * @return the node cast as a JSONArray
     */
    public default JSONArray asJSONArray() {
        return (JSONArray)this;
    }


}
