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
package org.ghotibeaun.json.factory;

import java.util.List;
import java.util.Map;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.converters.Converters;

public final class NodeFactory {


    public static JSONObject newJSONObject() {
        return new JSONObjectImpl();
    }

    public static JSONArray newJSONArray() {
        return new JSONArrayImpl();
    }

    public static JSONObject newJSONObject(Map<String, ?> map) {
        return Converters.convertToJSONObject(map);
    }

    public static JSONArray newJSONArray(List<Object> list) {
        return Converters.convertToJSONArray(list);
    }


    public static JSONValue<String> newStringValue(String value) {
        return new JSONStringValueImpl(value);
    }

    public static JSONValue<Number> newNumberValue(Number value) {
        return new JSONNumberValueImpl(value);
    }

    public static JSONValue<Boolean> newBooleanValue(boolean value) {
        return new JSONBooleanValueImpl(value);
    }

    public static JSONValue<JSONObject> newJSONObjectValue(JSONObject value) {
        return new JSONObjectValueImpl(value);
    }


    public static JSONValue<JSONArray> newJSONArrayValue(JSONArray value) {
        return new JSONArrayValueImpl(value);
    }


    public static JSONValue<NullObject> newJSONNullValue() {
        return new JSONNullValueImpl();
    }

    public static JSONNode parse(String data) {
        return JSONFactory.newFactory().newParser().parse(data);
    }




}
