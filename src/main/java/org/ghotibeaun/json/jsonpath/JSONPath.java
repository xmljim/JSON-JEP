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
     * @param jsonInputStream the JSON InputStream
     * @return a JSONArray of values. Values can be any valid JSON type including JSONArray or JSONObject instances
     */
    JSONArray evaluate(InputStream jsonInputStream);

    /**
     * Parses a JSON InputStream and returns a list of JSON values
     * @param jsonInputStream the JSON InputStream
     * @return a single value. If the JSONPath returns more than one result, then it returns only the first value;
     * If no values are returned than it returns null; When using primitive types (e.g., String, Boolean), take care
     * to understand the data, especially with any autoboxed values (double, boolean, int, etc.).
     */
    <T> T evaluateValue(InputStream jsonInputStream);
}
