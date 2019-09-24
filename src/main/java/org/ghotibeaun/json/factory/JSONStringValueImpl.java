package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValueType;

class JSONStringValueImpl extends AbstractJSONValue<String> {

    /**
     *
     */
    private static final long serialVersionUID = 5771553014612569673L;

    public JSONStringValueImpl() {
        super();
    }

    public JSONStringValueImpl(String value) {
        super(value);
    }

    @Override
    public JSONValueType getType() {
        return JSONValueType.STRING;
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
        return "\"" + getValue() + "\"";
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
