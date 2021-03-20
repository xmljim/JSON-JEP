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

import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.merge.MergeProcess;

/**
 * 
 * @author Jim Earley (xml.jim@gmail.com)
 *
 * @param <T> The JSONNode Type
 * @param <G> The property Getter data type, i.e., Integer for arrays, String for Arrays
 */
public interface ConflictStrategy<T, G> {

    MergeProcess getMergeProcessor();

    /**
     * Apply a conflict strategy to a given JSONNode context
     * @param context the JSONNode that receives the "resolved" value 
     * @param propertyValue either the key or index position of the value
     * @param primaryValue the value from the Primary JSONNode
     * @param secondaryValue the value from the Secondary JSONNode
     */
    void apply(T context, G propertyValue, JSONValue<?> primaryValue, JSONValue<?> secondaryValue);
}
