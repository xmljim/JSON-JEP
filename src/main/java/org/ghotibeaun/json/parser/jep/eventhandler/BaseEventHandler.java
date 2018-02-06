package org.ghotibeaun.json.parser.jep.eventhandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public abstract class BaseEventHandler extends EventHandler {

    private JSONValueType documentType = null;

    private final Deque<JSONValueType> objectStack = new ArrayDeque<>();
    private final Deque<String> keyStack = new ArrayDeque<>();
    private boolean keyBit = false;
    private boolean awaitingKey = false;
    private Object lastValue = null;
    private String lastKey = null;
    
    public BaseEventHandler() {
        
    }
    
    public JSONValueType getDocumentType() {
        return documentType;
    }
    
    @Override
    public void handleEvent(JSONEvent event) throws JSONEventParserException {
        switch (event.getEventType()) {
            case DOCUMENT_START:
                handleDocumentStart(getDataValue(event));
                break;
            case DOCUMENT_END:
                documentEnd();
                break;
            case OBJECT_START:
                objectStack.push(JSONValueType.OBJECT);
                setKeyBit(true);
                awaitingKey = true;
                jsonObjectStart(keyStack.peek());
                break;
            case OBJECT_END:
                objectStack.pop();
                final String objKey = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : (keyStack.size() > 0 ? keyStack.pop() : "$");
                jsonObjectEnd(objKey);
                break;
            case STRING_START:
                if (!isKey()) {
                    objectStack.push(JSONValueType.STRING);
                }
                break;
            case STRING_END:
                handleString(event, getDataValue(event));
                break;
            case ARRAY_START:
                objectStack.push(JSONValueType.ARRAY);
                jsonArrayStart(keyStack.peek());
                break;
            case ARRAY_END:
                jsonArrayEnd(handleValue(event, JSONValueType.ARRAY));
                break;
            case BOOLEAN_START:
                objectStack.push(JSONValueType.BOOLEAN);
                break;
            case BOOLEAN_END:
                valueBoolean(handleValue(event, JSONValueType.BOOLEAN), Boolean.getBoolean(getDataValue(event)));
                break;
            case NULL_START:
                objectStack.push(JSONValueType.NULL);
                break;
            case NULL_END:
                valueNull(handleValue(event, JSONValueType.NULL));
                break;
            case NUMBER_START:
                objectStack.push(JSONValueType.NUMBER);
                break;
            case NUMBER_END:
                handleNumber(event, getDataValue(event));
                break;
            case ENTITY_END:
                setKeyBit(true);
                awaitingKey = true;
                break;
            case KEY_END:
                setKeyBit(false);
                awaitingKey = false;
            default:
                
        }
        
    }
    
    
    private String getKeyValue() {
        return objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
    }
    


    private String handleValue(JSONEvent event, JSONValueType type) {
        final JSONValueType popped = objectStack.pop();
        if (popped != type) {
            throwError(event, "JSON Stack Error: Expected value type " + type + ", but saw " + popped);
        }
        
        awaitingKey = true;
        setKeyBit(true);
        return getKeyValue();
    }

    private void handleString(JSONEvent event, String data) {
        
        if (isKey()) {
            keyStack.push(data);
            awaitingKey = false;
            setKeyBit(false);
            newKey(data);
            
        } else {
            final JSONValueType popped = objectStack.pop();
            if (popped != JSONValueType.STRING) {
                throwError(event, "JSON Stack Error: Expected value type " + JSONValueType.STRING + ", but saw " + popped);
            }

            try {
                lastKey = keyStack.peek();
                final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
                this.valueString(key, data);
                lastValue = data;
                
            } catch (final Exception e) {
                System.err.println("ERRROR at token: " + data + "; lastSuccces {key: " + lastKey + ", value: " + lastValue.toString() + "}");
                e.printStackTrace();
                throw new RuntimeException(data, e);

            }
            
            awaitingKey = true;
            setKeyBit(true);
        }
    }
    
    private void handleNumber(JSONEvent event, String data) {
        final String key = handleValue(event, JSONValueType.NUMBER);
        
        if (data.contains(".")) {
            final BigDecimal bd = new BigDecimal(data);
            valueBigDecimal(key, bd);
        } else {
            final BigInteger bi = new BigInteger(data);
            valueLong(key, bi.longValue());
        }
    }

    private void handleDocumentStart(String data) {
        JSONValueType vt = null;
        if (data.charAt(0) == '{') {
            vt = JSONValueType.OBJECT;
            awaitingKey = true;
            setKeyBit(true);
        } else if (data.charAt(0) == '[') {
            vt = JSONValueType.ARRAY;
        }

        this.documentType = vt;
        objectStack.push(vt);

        //push the event to the appropriate method for consumers
        documentStart(vt);
    }

    private boolean isKey() {
        return (getKeyBit() && awaitingKey &&
                ((objectStack.peekFirst() != null) && (objectStack.peekFirst() == JSONValueType.OBJECT)));
    }
    
    private void setKeyBit(boolean bool) {
        this.keyBit = bool;
    }

    private boolean getKeyBit() {
        return this.keyBit;
    }

    private void throwError(JSONEvent event, String message) {
        throw new JSONEventParserException(event.getLineNumber(), event.getColumn(), message);
    }
    
    
}