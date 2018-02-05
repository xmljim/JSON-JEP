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
