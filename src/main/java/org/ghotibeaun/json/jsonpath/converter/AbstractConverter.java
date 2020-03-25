/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.jsonpath.converter;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.factory.NodeFactory;

/**
 * @author Jim Earley
 *
 */
public abstract class AbstractConverter<T> implements Converter<T> {

    /**
     *
     */
    public AbstractConverter() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.jsonpath.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public abstract T convert(Object data);

    public boolean isJSONArray(Object data) {
        return data instanceof JSONArray;
    }

    public boolean isJSONObject(Object data) {
        return data instanceof JSONObject;
    }

    public boolean isJSONValue(Object data) {
        return data instanceof JSONValue;
    }

    public boolean isPrimitive(Object data) {
        return NodeFactory.createFromObject(data).isPrimitive();
    }


}
