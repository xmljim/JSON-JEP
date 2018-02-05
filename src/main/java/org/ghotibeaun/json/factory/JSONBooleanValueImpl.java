package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValueType;

class JSONBooleanValueImpl extends AbstractJSONValue<Boolean> {

    public JSONBooleanValueImpl() {
        super();
    }

    public JSONBooleanValueImpl(Boolean val) {
        super(val);
    }

    @Override
    public JSONValueType getType() {
        return JSONValueType.BOOLEAN;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public String prettyPrint() {
        return toString();
    }

    @Override
    public String prettyPrint(int indent) {
        return toString();
    }

}
