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
package org.ghotibeaun.json.parser;

import java.math.BigDecimal;
import java.util.ArrayDeque;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.parser.jep.eventhandler.BaseEventHandler;

class NativeEventHandler extends BaseEventHandler {
    private JSONNode result;
    private JSONNode currentNode;
    private final ArrayDeque<JSONNode> stack = new ArrayDeque<>();

    private long totalAssemblyTime = 0;
    private long entities;

    public NativeEventHandler() {
    }

    public JSONNode getResult() {
        return result;
    }

    @Override
    public void documentStart(JSONValueType type) {
        final long s = System.nanoTime();
        if (type == JSONValueType.ARRAY) {
            result = NodeFactory.newJSONArray();
        } else if (type == JSONValueType.OBJECT) {
            result = NodeFactory.newJSONObject();
        }
        entities++;
        stack.push(result);
        currentNode = result;
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    private void appendToCurrent(String key, JSONArray array) {

        final long s = System.nanoTime();
        if (currentNode instanceof JSONArray) {
            ((JSONArray)currentNode).add(array);
        } else {
            ((JSONObject)currentNode).put(key, array);
        }
        entities++;
        stack.push(array);
        currentNode = array;
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    private void appendToCurrent(String key, JSONObject obj) {
        final long s = System.nanoTime();

        if (currentNode instanceof JSONArray) {
            ((JSONArray)currentNode).add(obj);
        } else {
            ((JSONObject)currentNode).put(key, obj);
        }
        entities++;
        stack.push(obj);
        currentNode = obj;

        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    private void appendToCurrent(String key, JSONValue<?> value) {
        final long s = System.nanoTime();
        if (currentNode instanceof JSONArray) {
            ((JSONArray)currentNode).add(value);
        } else {
            ((JSONObject)currentNode).put(key, value);
        }
        entities++;
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    @Override
    public void documentEnd() {
        System.out.println("Total Entities: " + entities);
        System.out.println("Time: " + totalAssemblyTime * .000000001f);

    }

    @Override
    public void jsonArrayStart(String key) {
        final long s= System.nanoTime();
        appendToCurrent(key, NodeFactory.newJSONArray());
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    @Override
    public void jsonArrayEnd(String key) {
        final long s = System.nanoTime();
        stack.pop();
        currentNode = stack.peek();
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;

    }

    @Override
    public void jsonObjectStart(String key) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newJSONObject());
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    @Override
    public void jsonObjectEnd(String key) {
        final long s = System.nanoTime();
        stack.pop();
        currentNode = stack.peek();
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    @Override
    public void valueString(String key, String value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newStringValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;
    }

    @Override
    public void valueLong(String key, Long value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newNumberValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;

    }

    @Override
    public void valueInt(String key, Integer value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newNumberValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;

    }

    @Override
    public void valueBigDecimal(String key, BigDecimal value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newNumberValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;

    }

    @Override
    public void valueDouble(String key, Double value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newNumberValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;    
    }

    @Override
    public void valueFloat(String key, Float value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newNumberValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;         
    }

    @Override
    public void valueBoolean(String key, boolean value) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newBooleanValue(value));
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;

    }

    @Override
    public void valueNull(String key) {
        final long s = System.nanoTime();
        appendToCurrent(key, NodeFactory.newJSONNullValue());
        final long e = System.nanoTime();
        totalAssemblyTime += e-s;

    }

    @Override
    public void newKey(String key) {
        // TODO Auto-generated method stub

    }


}
