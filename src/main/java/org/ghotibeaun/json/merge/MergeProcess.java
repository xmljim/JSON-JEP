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

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.merge.strategies.JSONArrayConflictStrategy;
import org.ghotibeaun.json.merge.strategies.JSONObjectConflictStrategy;

/**
 * Interface for merge processing
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public interface MergeProcess {

    /**
     * Return the specified merge result strategy
     */
    MergeResult getMergeResultStrategy();

    /**
     * Return the the Conflict Strategy to apply to JSONArray nodes
     */
    JSONArrayConflictStrategy getArrayConflictStrategy();

    /**
     * Return the Conflict Strategy to apply to JSONObject nodes
     */
    JSONObjectConflictStrategy getObjectConflictStrategy();

    /**
     * Return the merged result
     * @param <T> The JSON node type
     * @param primary the primary node in the merge operation
     * @param secondary the secondary node in the merge operation
     * @return the merged node
     */
    <T extends JSONNode> T mergeNodes(T primary, T secondary);
}
