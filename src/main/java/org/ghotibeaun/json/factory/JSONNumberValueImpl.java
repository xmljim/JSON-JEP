package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValueType;

class JSONNumberValueImpl extends AbstractJSONValue<Number> {

    public JSONNumberValueImpl() {
        // TODO Auto-generated constructor stub
    }

    public JSONNumberValueImpl(Number val) {
        super(val);
    }

    @Override
    public JSONValueType getType() {
        return JSONValueType.NUMBER;
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
