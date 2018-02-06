package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONValue;

class JSONArrayImpl extends AbstractJSONArray implements Iterable<JSONValue<?>> {

    public JSONArrayImpl() {
        super();
    }

    @Override
    public String toJSONString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < this.size(); i++) {
            builder.append(get(i).toString());
            if (i < (size() - 1)) {
                builder.append(",");
            }
        }

        builder.append("]");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public String prettyPrint() {
        return prettyPrint(0);
    }

    @Override
    public String prettyPrint(int indent) {
        final StringBuilder startStopIndent = new StringBuilder();
        for (int a = 0; a < ((indent * 4)); a++) {
            startStopIndent.append(" ");
        }

        final StringBuilder builder = new StringBuilder();
        final String indentString = "    ";

        builder.append("[");
        builder.append("\n");
        final int size = this.size();
        int pos = 0;

        for (int i = 0; i < this.size(); i++) {
            builder.append(startStopIndent.toString());
            builder.append(indentString);
            builder.append(get(i).prettyPrint(indent + 1));
            if (pos < (size - 1)) {
                builder.append(",");
            }
            pos++;
            builder.append("\n");
        }
        builder.append(startStopIndent.toString());
        builder.append("]");

        return builder.toString();

    }

}