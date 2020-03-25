/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.jsonpath;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Option;

/**
 * Factory for a JSONPath instance
 *
 * @author Jim Earley
 *
 */
public abstract class JSONPathFactory {

    /**
     *
     */
    private JSONPathFactory() {
        // TODO Auto-generated constructor stub
    }


    public static JSONPath compile(String jsonPath, Option... options) {
        final JSONPathImpl impl = new JSONPathImpl(jsonPath, options);
        return impl;
    }

    public static JSONPath compile(String jsonPath, Criteria criteria, Option...options) {
        final JSONPathImpl impl = new JSONPathImpl(jsonPath, criteria, options);
        return impl;
    }

}
