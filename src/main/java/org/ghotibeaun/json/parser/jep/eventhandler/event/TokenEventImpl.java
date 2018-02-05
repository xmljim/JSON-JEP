package org.ghotibeaun.json.parser.jep.eventhandler.event;

class TokenEventImpl extends JSONTokenEvent {
    
    public TokenEventImpl(JSONEventType type) {
        super(type);

    }

    public TokenEventImpl(JSONEventType type, int lineNumber, int column) {
        super(type, lineNumber, column);
    }
    
    
}
