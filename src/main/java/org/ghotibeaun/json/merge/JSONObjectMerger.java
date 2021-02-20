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
package org.ghotibeaun.json.merge;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.merge.strategies.JSONObjectConflictStrategy;

class JSONObjectMerger extends AbstractJSONObjectMerger {

    public JSONObjectMerger(JSONObjectConflictStrategy conflictStrategy, MergeProcess processor) {
        super(conflictStrategy, processor);
    }

    @Override
    public JSONObject merge(JSONObject primary, JSONObject secondary) {
        // TODO Auto-generated method stub
        if (primary.isEquivalent(secondary)) {
            return primary;
        } else {
            final JSONObject mergedObject = NodeFactory.newJSONObject();

            for (final String key : primary.keys()) {
                if (secondary.containsKey(key)) {
                    getConflictStrategy().apply(mergedObject, key, primary.get(key), secondary.get(key));
                } else {
                    mergedObject.put(key, primary.get(key));
                }
            }

            for (final String key: secondary.keys()) {
                if (!primary.containsKey(key)) {
                    mergedObject.put(key, secondary.get(key));
                }
            }

            return mergedObject;
        }
    }

}
