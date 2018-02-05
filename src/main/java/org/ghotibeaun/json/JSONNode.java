package org.ghotibeaun.json;

public interface JSONNode {

    String toJSONString();

    String prettyPrint();

    String prettyPrint(int indent);
}
