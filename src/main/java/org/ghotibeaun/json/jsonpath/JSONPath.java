package org.ghotibeaun.json.jsonpath;

import java.io.InputStream;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;

/**
 * Represents an JSONPath Expression to be evaluated.
 *
 * To implement a JSONPath query, use the {@linkplain JSONPathFactory} to create the instance, and apply the
 * {@linkplain JSONNode}, a JSON String, or JSON InputStream to the expression:
 * <pre>
 *      JSONNode books = ParserFactory.getFactory().newParser().parse(Paths.get("/books.json"));
 *      JSONPath jsonPath = JSONPathFactory.compile("$.store.book");
 *      JSONArray result = jsonPath.select(book);
 * </pre>
 *
 * There are additional convenience methods to execute a JSONPath expression without first instantiating a JSONNode
 * instance using any of the evaluate* methods:
 * <pre>
 *      Path bookPath = Paths.get("/books.json");
 *      try (InputStream bookStream = Files.newInputStream(bookPath);) {
 *          JSONPath jsonPath = JSONPathFactory.compile("$.store.book");
 *          JSONArray result = jsonPath.evaluate(bookStream);
 *      } catch (IOException e) {
 *          //handle the exception
 *      }
 * </pre>
 *
 *
 * @author Jim Earley
 *
 */
public interface JSONPath {


    /**
     * Return a list of JSON values from a JSONNode context.
     * @param context The JSONNode to execute the JSONPath expression on
     * @return a JSONArray of values. Values can be any valid JSON type including JSONArray or JSONObject instances
     */
    JSONArray select(JSONNode context);

    /**
     * Returns a single value from a JSONPath expression on a JSONNode context
     * @param context The JSONNode to execute the JSONPath expression on
     * @return a single value. If the JSONPath returns more than one result, then it returns only the first value;
     * If no values are returned than it returns null; When using primitive types (e.g., String, Boolean), take care
     * to understand the data, especially with any autoboxed values (double, boolean, int, etc.).
     */
    <T> T selectValue(JSONNode context);

    /**
     * Parses a JSON string and returns a list of JSON values
     * @param jsonString the JSON string data
     * @return a JSONArray of values. Values can be any valid JSON type including JSONArray or JSONObject instances
     */
    JSONArray evaluate(String jsonString);

    /**
     * Parses a JSON string and returns a list of JSON values
     * @param jsonString the JSON string data
     * @return a single value. If the JSONPath returns more than one result, then it returns only the first value;
     * If no values are returned than it returns null; When using primitive types (e.g., String, Boolean), take care
     * to understand the data, especially with any autoboxed values (double, boolean, int, etc.).
     */
    <T> T evaluateValue(String jsonString);

    /**
     * Parses a JSON InputStream and returns a list of JSON values
     * @param jsonInputString the JSON InputStream
     * @return a JSONArray of values. Values can be any valid JSON type including JSONArray or JSONObject instances
     */
    JSONArray evaluate(InputStream jsonInputStream);

    /**
     * Parses a JSON InputStream and returns a list of JSON values
     * @param jsonInputString the JSON InputStream
     * @return a single value. If the JSONPath returns more than one result, then it returns only the first value;
     * If no values are returned than it returns null; When using primitive types (e.g., String, Boolean), take care
     * to understand the data, especially with any autoboxed values (double, boolean, int, etc.).
     */
    <T> T evaluateValue(InputStream jsonInputStream);
}
