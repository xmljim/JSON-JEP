/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
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
        if (other.getValue() == null && getValue() == null) {
            return 0;
        } else if (other.getValue() == null || getValue() == null) {
            return 1;
        }

        return ((Comparable<T>)getValue()).compareTo(other.getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isEquivalent(JSONValue<?> other) {
        if (getType() == other.getType()) {
            return compareTo((JSONValue<T>)other) == 0;
        } else {
            return false;
        }
    }


}
