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
     * Factory for creating JSONPath queries
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
