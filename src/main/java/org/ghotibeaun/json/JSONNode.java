package org.ghotibeaun.json;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Path;

import org.ghotibeaun.json.jsonpath.JSONPath;
import org.ghotibeaun.json.jsonpath.JSONPathFactory;
import org.ghotibeaun.json.serializer.SerializationFactory;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Option;

/**
 * Base interface for JSON entities
 * @author Jim Earley
 *
 */
public interface JSONNode extends Serializable, Comparable<JSONNode> {

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
        return this instanceof JSONArray;
    }

    /**
     * Interrogate if the JSONNode is a JSONObject
     * @return true if it is a JSONObject; false otherwise
     */
    public default boolean isObject() {
        return this instanceof JSONObject;
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

    /**
     * Select a value from the JSONNode using a JSON-Path expression.
     * <p>
     * This is a wrapper around the {@linkplain JSONPath} API
     * </p>
     * @param jsonPath the JSON-Path expression
     * @return A JSONArray of values.
     */
    public default JSONArray select(String jsonPath) {
        return JSONPathFactory.compile(jsonPath).select(this);
    }

    /**
     * Select a value from the JSONNode using a JSON-Path expression.
     * <p>
     * This is a wrapper around the {@linkplain JSONPath} API
     * </p>
     * @param jsonPath the JSON-Path expression
     * @param options One or more options to implement on the JSONPath query
     * @return A JSONArray of values
     */
    public default JSONArray select(String jsonPath, Option...options) {
        return JSONPathFactory.compile(jsonPath, options).select(this);
    }

    /**
     * Select a value from the JSONNode using a JSON-Path expression.
     * <p>
     * This is a wrapper around the {@linkplain JSONPath} API
     * </p>
     * @param jsonPath the JSON-Path expression
     * @param options One or more options to implement on the JSONPath query
     * @return A JSONArray of values
     */
    public default JSONArray select(String jsonPath, Criteria criteria, Option...options) {
        return JSONPathFactory.compile(jsonPath, criteria, options).select(this);
    }

    /**
     * Select a single value from a JSONPath expression
     * @param jsonPath the JSONPath expression
     * @return the typed value (or null). For primitive Java types, users should take care to understand the data
     */
    public default <T> T selectValue(String jsonPath) {
        return JSONPathFactory.compile(jsonPath).selectValue(this);
    }

    public default <T> T selectValue(String jsonPath, Option...options) {
        return JSONPathFactory.compile(jsonPath, options).selectValue(this);
    }

    public default <T> T selectValue(String jsonPath, Criteria criteria, Option...options) {
        return JSONPathFactory.compile(jsonPath, criteria, options).selectValue(this);
    }

    public default void write(OutputStream out) {
        write(out, false);
    }

    public default void write(Path outputPath) {
        write(outputPath, false);
    }

    public default void write(Writer writer) {
        write(writer, false);
    }

    public default void write(File file) {
        write(file, false);
    }

    public default void write(OutputStream out, boolean prettyPrint) {
        SerializationFactory.getSerializer().write(out, this, prettyPrint);
    }

    public default void write(Path outputPath, boolean prettyPrint) {
        SerializationFactory.getSerializer().write(outputPath, this, prettyPrint);
    }

    public default void write(File outputFile, boolean prettyPrint) {
        SerializationFactory.getSerializer().write(outputFile, this, prettyPrint);
    }

    public default void write(Writer writer, boolean prettyPrint) {
        SerializationFactory.getSerializer().write(writer, this, prettyPrint);
    }

    /**
     * Compares against another JSONNode for equivalence. Equivalance is true if:
     * <ol>
     *  <li>Each JSONNode is of the same type</li>
     *  <li>For JSONObjects:
     *    <ul>
     *      <li>Must contain the same keys, though not necessarily in any order</li>
     *      <li>Each key's value must be equivalent</li>
     *    </ul>
     *  </li>
     *  <li>For JSONArrays:
     *    <ul>
     *      <li>List order does matter</li>
     *      <li>Each value in the list must be equivalent</li>
     *    </ul>
     *  </li>
     * </ol>
     * @param other The other JSONNode to compare against
     * @return true if both JSONNodes are equivalent; false otherwise
     */
    public default boolean isEquivalent(JSONNode other) {
        return compareTo(other) == 0;
    }

}
