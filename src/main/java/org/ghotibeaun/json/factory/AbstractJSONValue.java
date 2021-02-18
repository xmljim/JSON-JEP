package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.JSONValueType;

abstract class AbstractJSONValue<T> implements JSONValue<T> {

    /**
     *
     */
    private static final long serialVersionUID = -3400969671857411584L;
    private T _value;

    public AbstractJSONValue() {
        // no-op
    }

    public AbstractJSONValue(T val) {
        _value = val;
    }

    @Override
    public T getValue() {
        return _value;
    }

    @Override
    public abstract JSONValueType getType();

    @Override
    public abstract boolean isPrimitive();

    @Override
    public abstract boolean isArray();

    @Override
    public void setValue(T value) {
        _value = value;

    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(JSONValue<T> other) {
        return ((Comparable<T>)getValue()).compareTo(other.getValue());
    }




}
