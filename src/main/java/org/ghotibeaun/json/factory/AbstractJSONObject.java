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

import java.util.ArrayList;
import java.util.List;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

abstract class AbstractJSONObject extends AbstractMapNode implements JSONObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AbstractJSONObject() {
        super();
    }

    @Override
    public void put(String key, Number n) {
        if (n == null) {
            putNull(key);
            return;
        }

        final JSONValue<Number> value = NodeFactory.newNumberValue(n);
        put(key, value);
    }

    @Override
    public void put(String key, String s) {
        if (s == null) {
            putNull(key);
            return;
        }

        final JSONValue<String> value = NodeFactory.newStringValue(s);
        put(key, value);
    }

    @Override
    public void put(String key, boolean b) {

        final JSONValue<Boolean> value = NodeFactory.newBooleanValue(b);
        put(key, value);
    }

    @Override
    public void put(String key, JSONArray a) {
        if (a == null) {
            putNull(key);
            return;
        }
        final JSONValue<JSONArray> value = NodeFactory.newJSONArrayValue(a);
        put(key, value);
    }

    @Override
    public void put(String key, JSONObject o) {
        if (o == null) {
            putNull(key);
            return;
        }
        final JSONValue<JSONObject> value = NodeFactory.newJSONObjectValue(o);
        put(key, value);
    }

    @Override
    public void putNull(String key) {
        final JSONValue<NullObject> value = NodeFactory.newJSONNullValue();
        put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject getJSONObject(String key) {
        JSONObject value = null;
        if (containsKey(key)) {
            if (get(key).getType() == JSONValueType.OBJECT) {
                final JSONValue<JSONObject> v = (JSONValue<JSONObject>) get(key);
                value = v.getValue();
            } else {
                throw new JSONInvalidValueTypeException();
            }
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONArray getJSONArray(String key) {
        JSONArray value = null;

        if (containsKey(key)) {
            if (get(key).getType() == JSONValueType.ARRAY) {
                final JSONValue<JSONArray> v = (JSONValue<JSONArray>)get(key);
                value = v.getValue();
            }
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Number getNumber(String key) {
        Number value = null;
        if (containsKey(key)) {
            if (get(key).getType().isNumeric()) {
                final JSONValue<Number> v = (JSONValue<Number>)get(key);
                value = v.getValue();
            } else {
                throw new JSONInvalidValueTypeException("Cannot retrieve value as a number");
            }
        }

        return value;
    }

    @Override
    public long getLong(String key)  {
        long value;
        final Number v = getNumber(key);
        if (v != null) {
            value = v.longValue();
        } else {
            throw new JSONInvalidValueTypeException("Cannot cast a null value to an long");
        }
        return value;
    }

    @Override
    public int getInt(String key)  {
        int value;
        final Number v = getNumber(key);
        if (v != null) {
            value = v.intValue();
        } else {
            throw new JSONInvalidValueTypeException("Cannot cast a null value to an integer");
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean getBoolean(String key) {
        boolean value;

        if (containsKey(key)) {
            if (get(key).getType() == JSONValueType.BOOLEAN) {
                final JSONValue<Boolean> v = (JSONValue<Boolean>)get(key);
                value = v.getValue();
            } else {
                throw new JSONInvalidValueTypeException(JSONInvalidValueTypeException.getMessage(JSONValueType.BOOLEAN, get(key).getType()));
            }
        } else {
            throw new JSONInvalidValueTypeException("Cannot cast a null to a boolean");
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getString(String key) throws JSONInvalidValueTypeException {
        String value = null;
        if (containsKey(key)) {
            if (get(key).getType() == JSONValueType.STRING) {
                final JSONValue<String> v = (JSONValue<String>)get(key);
                value = v.getValue();
            } else {
                throw new JSONInvalidValueTypeException(JSONInvalidValueTypeException.getMessage(JSONValueType.STRING, get(key).getType()));
            }
        }
        return value;
    }

    @Override
    public JSONValueType getValueType(String key) {
        return get(key).getType();
    }

    @Override
    public String prettyPrint() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String prettyPrint(int indent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <V> List<V> valueList() {
        final List<V> list = new ArrayList<>();

        for (final JSONValue<?> item : values()) {
            @SuppressWarnings("unchecked")
            final
            JSONValue<V> cast = (JSONValue<V>)item;
            list.add(cast.getValue());
        }

        return list;
    }


    /* (non-Javadoc)
     * @see org.ghotibeaun.json.factory.AbstractJSONObject#compareTo(org.ghotibeaun.json.JSONNode)
     */
    @Override
    public int compareTo(JSONNode other) {
        if (other.isObject()) {
            final JSONObject compare = (JSONObject)other;

            if (this.size() == compare.size()) {
                int comp = 0;
                for (final String key : keys()) {
                    if (compare.containsKey(key)) {

                        final JSONValue<?> val = get(key);
                        final JSONValue<?> compValue = compare.get(key);

                        final boolean isEqual = val.isEquivalent(compValue);
                        comp += isEqual ? 0 : 1;


                        /*
                        if (val.getType().equals(compValue.getType())) {
                            if (val.isPrimitive()) {
                                if (val.getValue() == null && compValue.getValue() != null) {
                                    comp += 1;
                                } else if (val.getValue() != null && compValue.getValue() == null) {
                                    comp += 1;
                                } else if (val.getValue() == null && compValue.getValue() == null) {
                                    // nothing to do
                                } else {
                                    comp += val.getValue().equals(compValue.getValue()) ? 0 : 1;
                                }
                            } else {
                                comp += ((JSONNode)val.getValue()).compareTo((JSONNode)compValue.getValue());
                            }
                        } else {
                            comp += 1;
                        }
                         */
                    } else {
                        comp += 1;
                    }
                }

                return comp;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }



}
