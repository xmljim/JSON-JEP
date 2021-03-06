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
package org.ghotibeaun.json.factory;

import java.util.Map.Entry;

import org.ghotibeaun.json.JSONValue;

/**
 * Concrete implementation of the JSONObject interface
 * @author Jim Earley
 *
 */
class JSONObjectImpl extends AbstractJSONObject {

    /**
     *
     */
    private static final long serialVersionUID = -8276241950493713931L;

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
            if (pos < size - 1) {
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
        for (int a = 0; a < indent * 4; a++) {
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
            if (pos < size - 1) {
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
