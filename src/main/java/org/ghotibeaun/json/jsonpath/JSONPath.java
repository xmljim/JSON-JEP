package org.ghotibeaun.json.jsonpath;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Option;

public interface JSONPath {

    JSONArray evaluate(JSONNode context, String jsonPath);

    JSONArray evaluate(JSONNode context, String jsonPath, Option... option);

    JSONArray evaluate(JSONNode context, String jsonPath, Criteria filterCriteria);

    JSONArray evaluate(JSONNode context, String jsonPath, Criteria filterCriterea, Option...options);

}
