package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValueType;

class JSONNumberValueImpl extends AbstractJSONValue<Number> {

    /**
     *
     */
    private static final long serialVersionUID = 6941765393356792258L;

    public JSONNumberValueImpl() {
        // TODO Auto-generated constructor stub
    }

    public JSONNumberValueImpl(Number val) {
        super(val);
    }

    @Override
    public JSONValueType getType() {
        final String typeName = getValue().getClass().getTypeName();

        if (typeName.toUpperCase().contains("INT")) {
            return JSONValueType.INTEGER;
        } else if (typeName.toUpperCase().contains("LONG")) {
            return JSONValueType.LONG;
        } else if (typeName.toUpperCase().contains("DOUBLE")) {
            return JSONValueType.DOUBLE;
        } else if (typeName.toUpperCase().contains("FLOAT")) {
            return JSONValueType.FLOAT;
        } else {
            return JSONValueType.NUMBER;
        }
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
