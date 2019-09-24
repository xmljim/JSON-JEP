package org.ghotibeaun.json;

import java.io.Serializable;

public interface JSONValue<T> extends Serializable {
    T getValue();

    @Override
    String toString();

    JSONValueType getType();

    boolean isPrimitive();

    boolean isArray();

    void setValue(T value);

    String prettyPrint();

    String prettyPrint(int indent);


}
