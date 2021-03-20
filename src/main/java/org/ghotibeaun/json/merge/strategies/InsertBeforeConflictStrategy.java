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
package org.ghotibeaun.json.merge.strategies;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.merge.MergeProcess;

public class InsertBeforeConflictStrategy extends AbstractJSONArrayConflictStrategy {

    public InsertBeforeConflictStrategy(MergeProcess mergeProcessor) {
        super(mergeProcessor);
    }

    @Override
    public void apply(JSONArray context, Integer propertyValue, JSONValue<?> primaryValue, JSONValue<?> secondaryValue) {

        if (primaryValue.isEquivalent(secondaryValue)) {
            context.add(primaryValue);
            return;
        }

        if (primaryValue.getType() == secondaryValue.getType()) {
            if (primaryValue.isPrimitive()) {
                context.add(secondaryValue);
                context.add(primaryValue);
            } else if (primaryValue.isObject()) {
                final JSONObject mergedObject = getMergeProcessor().mergeNodes((JSONObject)primaryValue.getValue(), 
                        (JSONObject)secondaryValue.getValue());
                context.add(mergedObject);
            } else {
                final JSONArray mergedArray = getMergeProcessor().mergeNodes((JSONArray)primaryValue.getValue(), 
                        (JSONArray)secondaryValue.getValue());
                context.add(mergedArray);
            }
        } else {
            context.add(secondaryValue);
            context.add(primaryValue);
        }

    }
}
