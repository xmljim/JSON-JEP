package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONValueType;

class JSONArrayValueImpl extends AbstractJSONValue<JSONArray> {

    /**
     *
     */
    private static final long serialVersionUID = -4412270827824841485L;

    public JSONArrayValueImpl() {
        super();
    }

    public JSONArrayValueImpl(JSONArray array) {
        super(array);
    }

    @Override
    public JSONValueType getType() {
        return JSONValueType.ARRAY;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public String toString() {
        return getValue().toJSONString();
    }

    @Override
    public String prettyPrint() {
        return getValue().prettyPrint();

    }

    @Override
    public String prettyPrint(int indent) {
        return getValue().prettyPrint(indent);
    }

}
