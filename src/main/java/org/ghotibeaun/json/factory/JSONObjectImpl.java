package org.ghotibeaun.json.factory;

import java.util.Map.Entry;

import org.ghotibeaun.json.JSONValue;

class JSONObjectImpl extends AbstractJSONObject {

    public JSONObjectImpl() {
        super();
    }

    @Override
    public String toJSONString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        final int size = this.size();
        int pos = 0;
        for (final Entry<String, JSONValue<?>> entry : elements()) {
            final String key = entry.getKey();
            final String value = entry.getValue().toString();
            builder.append("\"" + key + "\"").append(":").append(value);
            if (pos < (size - 1)) {
                builder.append(",");
            }
            pos++;
        }

        builder.append("}");

        return builder.toString();
    }

    @Override
    public String toString() {
        return toJSONString();
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

        builder.append("{");
        builder.append("\n");
        final int size = this.size();
        int pos = 0;
        final String indentString = "    ";
        for (final Entry<String, JSONValue<?>> entry : elements()) {
            final String key = entry.getKey();
            final String value = entry.getValue().prettyPrint(indent + 1);
            builder.append(startStopIndent.toString());
            builder.append(indentString).append("\"" + key + "\"").append(": ").append(value);
            if (pos < (size - 1)) {
                builder.append(",");
            }

            pos++;
            builder.append("\n");
        }
        builder.append(startStopIndent.toString());
        builder.append("}");

        return builder.toString();
    }
}
