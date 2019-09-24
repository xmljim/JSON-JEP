package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.NullObject;

class JSONNullValueImpl extends AbstractJSONValue<NullObject> {

    /**
     *
     */
    private static final long serialVersionUID = 343392039089034698L;

    @Override
    public JSONValueType getType() {
        return JSONValueType.NULL;
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
    public NullObject getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
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
