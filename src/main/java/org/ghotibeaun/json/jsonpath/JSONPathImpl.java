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
package org.ghotibeaun.json.jsonpath;

import java.io.InputStream;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.converters.Converters;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.ParserFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

class JSONPathImpl implements JSONPath {
    private Configuration configuration;

    private final String jsonPath;
    private final Criteria criteria;


    protected JSONPathImpl(String jsonPath, Option...options) {
        initConfiguration(options);
        this.jsonPath = jsonPath;
        criteria = null;
    }

    protected JSONPathImpl(String jsonPath, Criteria criteria, Option...options) {
        initConfiguration(options);
        this.jsonPath = jsonPath;
        this.criteria = criteria;
    }

    private String getJsonPath() {
        return jsonPath;
    }

    private Criteria getCriteria() {
        return criteria;
    }


    private void initConfiguration(Option...options) {
        configuration = Configuration.builder()
                .jsonProvider(FactorySettings.createFactoryClass(Setting.JSONPATH_PROVIDER_CLASS))
                .mappingProvider(FactorySettings.createFactoryClass(Setting.JSONPATH_MAPPING_PROVIDER_CLASS))
                .build();

        if (options.length > 0) {
            configuration.addOptions(options);
        } else {
            configuration.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        }
    }



    @Override
    public JSONArray select(JSONNode context) {
        return doEvaluate(context);
    }

    @Override
    public <T> T selectValue(JSONNode context) {
        final JSONArray values = select(context);

        if (values.size() > 0) {
            return values.getValue(0);
        } else {
            return null;
        }
    }



    @Override
    public JSONArray evaluate(String jsonString) {

        final JSONNode node = NodeFactory.parse(jsonString);
        return select(node);
    }


    @Override
    public JSONArray evaluate(InputStream jsonInputStream) {
        final JSONNode node = ParserFactory.getParser().parse(jsonInputStream);
        return select(node);
    }

    private JSONArray doEvaluate(JSONNode context) {

        if (getCriteria() != null) {
            return JsonPath.using(configuration).parse(context).read(getJsonPath(), Filter.filter(getCriteria()));

        } else {
            final Object o = JsonPath.using(configuration).parse(context).read(getJsonPath());
            final JSONValue<?> val = Converters.convertToJSONValue(o, Optional.empty(), Optional.empty());//NodeFactory.createFromObject(o);

            if (!val.isArray()) {
                final JSONArray array = NodeFactory.newJSONArray();
                array.add(val);
                return array;
            } else {
                return (JSONArray)val.getValue();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.jsonpath.JSONPath#evaluateValue(java.lang.String)
     */
    @Override
    public <T> T evaluateValue(String jsonString) {
        final JSONArray data = evaluate(jsonString);
        if (data.size() > 0) {
            return data.getValue(0);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.jsonpath.JSONPath#evaluateValue(java.io.InputStream)
     */
    @Override
    public <T> T evaluateValue(InputStream jsonInputStream) {
        final JSONArray data = evaluate(jsonInputStream);
        if (data.size() > 0) {
            return data.getValue(0);
        } else {
            return null;
        }
    }
}
