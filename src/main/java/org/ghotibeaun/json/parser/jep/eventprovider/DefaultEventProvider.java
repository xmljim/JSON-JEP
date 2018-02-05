package org.ghotibeaun.json.parser.jep.eventprovider;

import java.nio.ByteBuffer;

import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType;

class DefaultEventProvider extends EventProvider {
    
    
    public DefaultEventProvider() {
        super();
    }
    
    @Override
    public void notifyDocumentStart(ByteBuffer start) {
        final JSONEvent event = JSONEvent.newDataEvent(start, JSONEventType.DOCUMENT_START);
        notifyEvent(event);
    }
    
    @Override
    public void notifyDocumentEnd() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.DOCUMENT_END);
        notifyEvent(event);
    }
    
    @Override
    public void notifyStringTokenStart() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.STRING_START);
        notifyEvent(event);
    }
    
    @Override
    public void notifyStringTokenEnd(ByteBuffer tokenValue) {
        final JSONEvent event = JSONEvent.newDataEvent(tokenValue, JSONEventType.STRING_END);
        getEventHandler().handleEvent(event);

    }
    
    @Override
    public void notifyBooleanTokenStart() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.BOOLEAN_START);
        notifyEvent(event);

    }
    
    @Override
    public void notifyBooleanTokenEnd(ByteBuffer tokenValue) {
        final JSONEvent event = JSONEvent.newDataEvent(tokenValue, JSONEventType.BOOLEAN_END);
        notifyEvent(event);

    }
    
    @Override
    public void notifyNumberTokenStart() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.NUMBER_START);
        notifyEvent(event);

    }
    
    @Override
    public void notifyNumberTokenEnd(ByteBuffer tokenValue) {
        final JSONEvent event = JSONEvent.newDataEvent(tokenValue, JSONEventType.BOOLEAN_END);
        getEventHandler().handleEvent(event);

    }
    
    @Override
    public void notifyNullTokenStart() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.NULL_START);
        notifyEvent(event);
    }
    
    @Override
    public void notifyNullTokenEnd(ByteBuffer tokenValue) {
        final JSONEvent event = JSONEvent.newDataEvent(tokenValue, JSONEventType.NULL_END);
        notifyEvent(event);

    }
    
    @Override
    public void notifyMapStart() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.OBJECT_START);
        notifyEvent(event);

    }
    
    @Override
    public void notifyMapEnd() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.OBJECT_END);
        notifyEvent(event);
    }
    
    @Override
    public void notifyKeyEnd() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.KEY_END);
        notifyEvent(event);

    }
    
    @Override
    public void notifyArrayStart() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.ARRAY_START);
        notifyEvent(event);

    }
    
    @Override
    public void notifyArrayEnd() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.ARRAY_END);
        notifyEvent(event);

    }
    
    @Override
    public void notifyEntityEnd() {
        final JSONEvent event = JSONEvent.newTokenEvent(JSONEventType.ENTITY_END);
        notifyEvent(event);

    }
    
    @Override
    public void notifyEvent(JSONEvent event) {
        getEventHandler().handleEvent(event);

    }
    

    
    
}
