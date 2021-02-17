package org.ghotibeaun.json.factory;

import java.util.List;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

abstract class AbstractJSONArray extends AbstractListNode implements JSONArray, Iterable<JSONValue<?>> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AbstractJSONArray() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getString(int index) throws JSONInvalidValueTypeException {
        String value = null;
        if (get(index).getType() == JSONValueType.STRING) {
            final JSONValue<String> v = (JSONValue<String>) get(index);
            value = v.getValue();
        } else {
            throw new JSONInvalidValueTypeException(
                    JSONInvalidValueTypeException.getMessage(JSONValueType.STRING, get(index).getType()));
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Number getNumber(int index) throws JSONInvalidValueTypeException {
        Number value = null;
        if (get(index).getType() == JSONValueType.NUMBER) {
            final JSONValue<Number> v = (JSONValue<Number>) get(index);
            value = v.getValue();
        } else {
            throw new JSONInvalidValueTypeException(
                    JSONInvalidValueTypeException.getMessage(JSONValueType.NUMBER, get(index).getType()));
        }

        return value;
    }

    @Override
    public long getLong(int index) throws JSONInvalidValueTypeException {
        return getNumber(index).longValue();
    }

    @Override
    public int getInt(int index) throws JSONInvalidValueTypeException {
        return getNumber(index).intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean getBoolean(int index) throws JSONInvalidValueTypeException {
        Boolean value = null;
        if (get(index).getType() == JSONValueType.BOOLEAN) {
            final JSONValue<Boolean> v = (JSONValue<Boolean>) get(index);
            value = v.getValue();
        } else {
            throw new JSONInvalidValueTypeException(
                    JSONInvalidValueTypeException.getMessage(JSONValueType.BOOLEAN, get(index).getType()));
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONArray getJSONArray(int index) throws JSONInvalidValueTypeException {
        JSONArray value = null;
        if (get(index).getType() == JSONValueType.ARRAY) {
            final JSONValue<JSONArray> v = (JSONValue<JSONArray>) get(index);
            value = v.getValue();
        } else {
            throw new JSONInvalidValueTypeException(
                    JSONInvalidValueTypeException.getMessage(JSONValueType.ARRAY, get(index).getType()));
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject getJSONObject(int index) throws JSONInvalidValueTypeException {
        JSONObject value = null;
        if (get(index).getType() == JSONValueType.OBJECT) {
            final JSONValue<JSONObject> v = (JSONValue<JSONObject>) get(index);
            value = v.getValue();
        } else {
            throw new JSONInvalidValueTypeException(
                    JSONInvalidValueTypeException.getMessage(JSONValueType.OBJECT, get(index).getType()));
        }

        return value;
    }

    @Override
    public void add(String string) {
        final JSONValue<String> value = NodeFactory.newStringValue(string);
        add(value);

    }

    @Override
    public void add(Boolean bool) {
        final JSONValue<Boolean> value = NodeFactory.newBooleanValue(bool);
        add(value);
    }

    @Override
    public void add(Number number) {
        final JSONValue<Number> value = NodeFactory.newNumberValue(number);
        add(value);

    }

    @Override
    public void add(JSONArray array) {
        final JSONValue<JSONArray> value = NodeFactory.newJSONArrayValue(array);
        add(value);

    }

    @Override
    public void add(JSONObject obj) {
        final JSONValue<JSONObject> value = NodeFactory.newJSONObjectValue(obj);
        add(value);

    }

    @Override
    public JSONValueType getValueType(int index) {
        return get(index).getType();
    }

    @Override
    public abstract String toJSONString();

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.JSONListNode#toList()
     */
    @Override
    public <V> List<V> toList() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.JSONNode#prettyPrint(int)
     */
    @Override
    public abstract String prettyPrint(int indent);

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.factory.AbstractJSONNode#compareTo(org.ghotibeaun.json.JSONNode)
     */
    @Override
    public int compareTo(JSONNode other) {
        if (other.isArray()) {
            final JSONArray compare = (JSONArray)other;
            int comp = 0;

            for (int i = 0; i < size(); i++) {
                final JSONValue<?> val = get(i);
                final JSONValue<?> compValue = compare.get(i);

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
            }

            return comp;
        } else {
            return 1;
        }
    }


}
