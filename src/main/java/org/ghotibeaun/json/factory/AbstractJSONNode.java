package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONValue;

abstract class AbstractJSONNode implements JSONNode {
    public AbstractJSONNode() {

    }

    public abstract JSONValue<?> getValue();

    @Override
    public String toJSONString() {
        return getValue().toString();
    }

    @Override
    public abstract String prettyPrint();

}
