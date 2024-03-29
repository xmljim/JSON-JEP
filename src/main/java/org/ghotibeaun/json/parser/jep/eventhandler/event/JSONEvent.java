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
package org.ghotibeaun.json.parser.jep.eventhandler.event;

import java.nio.ByteBuffer;

public abstract class JSONEvent {

    private int lineNumber;
    private int column;

    protected JSONEvent() {

    }

    protected JSONEvent(int lineNumber, int column) {
        this.lineNumber = lineNumber;
        this.column = column;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    public abstract JSONEventType getEventType();

    public abstract ByteBuffer getData();

    public static JSONEvent newDataEvent(ByteBuffer data, JSONEventType type) {
        return new DataEventImpl(data, type);
    }

    public static JSONEvent newDataEvent(ByteBuffer data, JSONEventType type, int lineNumber, int column) {
        return new DataEventImpl(data, type, lineNumber, column);
    }

    public static JSONEvent newTokenEvent(JSONEventType type) {
        return new TokenEventImpl(type);
    }

    public static JSONEvent newTokenEvent(JSONEventType type, int lineNumber, int column) {
        return new TokenEventImpl(type, lineNumber, column);
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append(getEventType()).append(" '");

        if (getData() == null) {
            builder.append("[no data]");
        } else {
            builder.append(new String(getData().array()));
        }

        builder.append("' [line: ").append(getLineNumber());
        builder.append("; col: ").append(getColumn());
        builder.append("]");

        return builder.toString();

        //return ((getEventType() + " '" + (getData())) != null ? new String(getData().array()): "[no data]") + "' [line: " + getLineNumber() + "; col: " + getColumn() + "]";
    }


}
