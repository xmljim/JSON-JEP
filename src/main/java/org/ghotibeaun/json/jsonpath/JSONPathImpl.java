package org.ghotibeaun.json.jsonpath;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

class JSONPathImpl implements JSONPath {
    private final Configuration configuration = Configuration.defaultConfiguration();

    public JSONPathImpl() {
        //no-op
    }

    @Override
    public JSONArray evaluate(JSONNode context, String jsonPath) {
        final Option[] options = new Option[0];
        return evaluate(context, jsonPath, options);
    }

    @Override
    public JSONArray evaluate(JSONNode context, String jsonPath, Option... options) {
        configuration.addOptions(options);
        JSONArray result = null;
        result = JsonPath.read(context, jsonPath);
        return result;
    }

    @Override
    public JSONArray evaluate(JSONNode context, String jsonPath, Criteria filterCriteria) {
        final Option[] options = new Option[0];
        return evaluate(context, jsonPath, filterCriteria, options);
    }

    @Override
    public JSONArray evaluate(JSONNode context, String jsonPath, Criteria filterCriteria, Option... options) {
        configuration.addOptions(options);
        final Filter queryFilter = Filter.filter(filterCriteria);
        final JSONArray result = JsonPath.read(context, jsonPath, queryFilter);
        return result;

    }

}
