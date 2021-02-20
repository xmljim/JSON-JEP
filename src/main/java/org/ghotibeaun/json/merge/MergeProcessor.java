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

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.merge.strategies.AcceptPrimaryConflictStrategy;
import org.ghotibeaun.json.merge.strategies.AcceptSecondaryConflictStrategy;
import org.ghotibeaun.json.merge.strategies.AppendArrayConflictStrategy;
import org.ghotibeaun.json.merge.strategies.AppendObjectConflictStrategy;
import org.ghotibeaun.json.merge.strategies.ArrayConflict;
import org.ghotibeaun.json.merge.strategies.DeduplicateArrayConflictStrategy;
import org.ghotibeaun.json.merge.strategies.InsertAfterConflictStrategy;
import org.ghotibeaun.json.merge.strategies.InsertBeforeConflictStrategy;
import org.ghotibeaun.json.merge.strategies.JSONArrayConflictStrategy;
import org.ghotibeaun.json.merge.strategies.JSONObjectConflictStrategy;
import org.ghotibeaun.json.merge.strategies.ObjectConflict;

public final class MergeProcessor implements MergeProcess {
    private JSONArrayConflictStrategy arrayStrategy;
    private JSONObjectConflictStrategy objectStrategy;
    private final MergeResult mergeResult;

    /**
     * Private constructor. Use static methods to instantiate and run Merge
     * @param arrayStrat The Array Conflict Strategy
     * @param objectStrat The Object Conflict Strategy
     * @param result The merge result strategy
     */
    private MergeProcessor(ArrayConflict arrayStrat, ObjectConflict objectStrat, MergeResult result) {
        mergeResult = result;

        switch(objectStrat) {
            case ACCEPT_SECONDARY:
                objectStrategy = new AcceptSecondaryConflictStrategy(this);
                break;
            case ACCEPT_PRIMARY:
                objectStrategy = new AcceptPrimaryConflictStrategy(this);
                break;
            case APPEND:
                objectStrategy = new AppendObjectConflictStrategy(this);
                break;
        }

        switch (arrayStrat) {
            case INSERT_AFTER:
                arrayStrategy = new InsertAfterConflictStrategy(this);
                break;
            case INSERT_BEFORE:
                arrayStrategy = new InsertBeforeConflictStrategy(this);
                break;
            case APPEND:
                arrayStrategy = new AppendArrayConflictStrategy(this);
                break;
            case DEDUPLICATE:
                arrayStrategy = new DeduplicateArrayConflictStrategy(this);
                break;
        }
    }

    /**
     * Invoke and return a merge JSON instance
     * @param <T> The JSONNode type 
     * @param primary the Primary source
     * @param secondary The secondary instance
     * @param arrayStrategies The Array Conflict strategy
     * @param objectStrategy The Object Conflict strategy
     * @param mergeResultStrategy the merge result strategy.
     * @return a merged JSON instance.
     */
    public static <T extends JSONNode> T merge(T primary, T secondary, ArrayConflict arrayStrategies,
            ObjectConflict objectStrategy, MergeResult mergeResultStrategy) {

        final MergeProcessor mp = new MergeProcessor(arrayStrategies, objectStrategy, mergeResultStrategy);
        final T result = mp.mergeNodes(primary, secondary);
        if (mp.getMergeResultStrategy() == MergeResult.MERGE_PRIMARY) {
            primary = result;
            return primary;
        } else {
            return result;
        }
    }

    /**
     * Merge two JSON instances
     * @param <T> The JSON Node type e.g., {@link JSONArray} {@link JSONObject}
     * @param primary The primary instance
     * @param secondary the secondary instance
     * @param mergeResultStrategy The merge result strategy
     * @return
     */
    public static <T extends JSONNode> T merge(T primary, T secondary, MergeResult mergeResultStrategy) {
        return merge(primary, secondary, ArrayConflict.APPEND, ObjectConflict.ACCEPT_PRIMARY, mergeResultStrategy);
    }

    public static <T extends JSONNode> T merge(T primary, T secondary, ArrayConflict array, ObjectConflict object) {
        return merge(primary, secondary, array, object, MergeResult.NEW_INSTANCE);
    }

    public static <T extends JSONNode> T merge(T primary, T secondary, ArrayConflict array) {
        return merge(primary, secondary, array, ObjectConflict.ACCEPT_PRIMARY);
    }

    public static <T extends JSONNode> T merge(T primary, T secondary, ObjectConflict object) {
        return merge(primary, secondary, ArrayConflict.APPEND, object);
    }

    public static <T extends JSONNode> T merge(T primary, T secondary) {
        return merge(primary, secondary, MergeResult.NEW_INSTANCE);
    }

    @Override
    public MergeResult getMergeResultStrategy() {
        return mergeResult;
    }

    @Override
    public JSONArrayConflictStrategy getArrayConflictStrategy() {
        return arrayStrategy;
    }

    @Override
    public JSONObjectConflictStrategy getObjectConflictStrategy() {
        return objectStrategy;
    }

    @Override
    public <T extends JSONNode> T mergeNodes(T primary, T secondary) {
        @SuppressWarnings("unchecked")
        final Merger<T, ?> merger = (Merger<T, ?>) getMerger(primary);
        return merger.merge(primary, secondary);
    }

    private Merger<?, ?> getMerger(JSONNode node) {
        if (node.isArray()) {
            return new JSONArrayMerger(getArrayConflictStrategy(), this);
        } else {
            return new JSONObjectMerger(getObjectConflictStrategy(), this);
        }
    }
}
