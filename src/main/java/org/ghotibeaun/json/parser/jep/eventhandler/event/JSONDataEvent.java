package org.ghotibeaun.json.parser.jep.eventhandler.event;

import java.nio.ByteBuffer;

public abstract class JSONDataEvent extends JSONTokenEvent {
    
    private final ByteBuffer data;
    
    protected JSONDataEvent(JSONEventType type, ByteBuffer data) {
        super(type);
        this.data = data;
    }
    
    protected JSONDataEvent(JSONEventType type, ByteBuffer data, int lineNumber, int column) {
        super(type, lineNumber, column);
        this.data = data;
    }

    
    @Override
    public ByteBuffer getData() {
        return data;
    }
    
}
