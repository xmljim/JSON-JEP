package org.ghotibeaun.json.factory;

import java.util.ArrayList;
import java.util.List;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.NullObject;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

class AbstractJSONObject extends AbstractMapNode implements JSONObject {

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



}
