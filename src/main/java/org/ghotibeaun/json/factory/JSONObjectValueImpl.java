package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValueType;

class JSONObjectValueImpl extends AbstractJSONValue<JSONObject> {

    /**
     *
     */
    private static final long serialVersionUID = 2645124763479067819L;

    public JSONObjectValueImpl() {

    }

    public JSONObjectValueImpl(JSONObject value) {
        super(value);

    }

    @Override
    public JSONValueType getType() {
        return JSONValueType.OBJECT;
    }

    @Override
    public String toString() {
        return getValue().toJSONString();
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
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
