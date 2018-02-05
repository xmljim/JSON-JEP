package org.ghotibeaun.json;

public interface JSONValue<T> {
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
