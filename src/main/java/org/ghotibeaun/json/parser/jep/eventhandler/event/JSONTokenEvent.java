package org.ghotibeaun.json.parser.jep.eventhandler.event;

import java.nio.ByteBuffer;

public abstract class JSONTokenEvent extends JSONEvent {

    private final JSONEventType type;
    
    protected JSONTokenEvent(JSONEventType type) {
        super();
        this.type = type;
    }

    protected JSONTokenEvent(JSONEventType type, int lineNumber, int column) {
        super(lineNumber, column);
        this.type = type;
    }

    @Override
    public JSONEventType getEventType() {
        return type;
    }

    @Override
    public ByteBuffer getData() {
        return null;
    }

}
