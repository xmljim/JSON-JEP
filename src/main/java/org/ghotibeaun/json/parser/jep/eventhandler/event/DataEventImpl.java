package org.ghotibeaun.json.parser.jep.eventhandler.event;

import java.nio.ByteBuffer;

class DataEventImpl extends JSONDataEvent {
    
    

    public DataEventImpl(ByteBuffer data, JSONEventType type) {
        super(type, data);
        
    }

    public DataEventImpl(ByteBuffer data, JSONEventType type, final int lineNumber, final int column) {
        super(type, data, lineNumber, column);
        
    }
    

}
