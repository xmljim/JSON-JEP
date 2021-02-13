package org.ghotibeaun.json;

public enum JSONValueType {
    BOOLEAN(false), 
    STRING(false), 
    NUMBER(true),
    INTEGER(true),
    LONG(true),
    DOUBLE(true),
    FLOAT(true),
    ARRAY(false), 
    OBJECT(false), 
    DATE(false), 
    NULL(false),
    UNKNOWN(false); //only used by csv processor

    boolean isNumeric;

    private JSONValueType(boolean numeric) {
        isNumeric = numeric;
    }

    public boolean isNumeric() {
        return isNumeric;
    }
}


