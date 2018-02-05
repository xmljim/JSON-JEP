package org.ghotibeaun.json.parser.jep.eventprovider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;

import org.ghotibeaun.json.JSONValueType;
import org.ghotibeaun.json.parser.jep.eventhandler.JSONEventHandler;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

/**
 * This provider invokes the underlying {@link JSONEventHandler} event methods and also uses the native VM CharacterSet decode to convert
 * any data to String
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
class DirectEventProviderImpl extends EventProvider {
    
    private JSONValueType documentType = null;
    
    private final Deque<JSONValueType> objectStack = new ArrayDeque<>();
    private final Deque<String> keyStack = new ArrayDeque<>();
    private boolean keyBit = false;
    private boolean awaitingKey = false;
    private Object lastValue = null;
    private String lastKey = null;

    public DirectEventProviderImpl() {
        super();
    }

    @Override
    public void notifyDocumentStart(ByteBuffer c) {
        
        if (getEventHandler() != null) {
            JSONValueType vt = null;
            if (c.getChar() == '{') {
                vt = JSONValueType.OBJECT;
                awaitingKey = true;
                setKeyBit(true);
            } else if (c.getChar() == '[') {
                vt = JSONValueType.ARRAY;
            }
            
            this.documentType = vt;
            objectStack.push(vt);
            getEventHandler().documentStart(this.documentType);
        }

    }
    
    public JSONValueType getDocumentType() {
        return this.documentType;
    }

    @Override
    public void notifyDocumentEnd() {
        getEventHandler().documentEnd();
    }

    @Override
    public void notifyStringTokenStart() {
        if (!isKey()) {
            objectStack.push(JSONValueType.STRING);
        }
    }

    @Override
    public void notifyStringTokenEnd(ByteBuffer tokenValue) {
        final String stringValue = new String(tokenValue.asCharBuffer().array());
        if (isKey()) {
            keyStack.push(stringValue);
            awaitingKey = false;
            if (getEventHandler() != null) {
                getEventHandler().newKey(stringValue);
            }
        } else {
            if (objectStack.pop() != JSONValueType.STRING) {
                //TODO: error should match
            }

            try {
                lastKey = keyStack.peek();
                final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
                
                if (getEventHandler() != null) {
                    getEventHandler().valueString(key, stringValue);
                    lastValue = stringValue;

                }
            } catch (final Exception e) {
                System.err.println("ERRROR at token: " + stringValue + "; lastSuccces {key: " + lastKey + ", value: " + lastValue.toString() + "}");
                e.printStackTrace();
                throw new RuntimeException(stringValue, e);

            }
            awaitingKey = true;
            setKeyBit(true);
        }
        
        
    }

    @Override
    public void notifyBooleanTokenStart() {
        objectStack.push(JSONValueType.BOOLEAN);
    }

    @Override
    public void notifyBooleanTokenEnd(ByteBuffer tokenValue) {
        if (objectStack.pop() != JSONValueType.BOOLEAN) {
            //TODO: ERROR should match;
        }
        
        final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
        
        if (getEventHandler() != null) {
            final String boolValue = new String(tokenValue.asCharBuffer().array());
            getEventHandler().valueBoolean(key, Boolean.getBoolean(boolValue));
        }

        awaitingKey = true;
        setKeyBit(true);
    }

    @Override
    public void notifyNumberTokenStart() {
        objectStack.push(JSONValueType.NUMBER);
    }

    @Override
    public void notifyNumberTokenEnd(ByteBuffer tokenValue) {
        if (getEventHandler() != null) {
            
            if (objectStack.pop() != JSONValueType.NUMBER) {
                //TODO: Error should match;
            }

            final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
            
            final String numberValue = new String(tokenValue.asCharBuffer().array());
            if (numberValue.contains(".") || numberValue.contains("e")) {
                final BigDecimal bd = new BigDecimal(numberValue);
                getEventHandler().valueBigDecimal(key, bd);
            } else {
                final BigInteger bi = new BigInteger(numberValue);
                getEventHandler().valueLong(key, bi.longValue());
            }
            
        }
        
        awaitingKey = true;
        setKeyBit(true);

    }

    @Override
    public void notifyNullTokenStart() {
        objectStack.push(JSONValueType.NULL);
    }

    @Override
    public void notifyNullTokenEnd(ByteBuffer tokenValue) {
        if (objectStack.pop() != JSONValueType.NULL) {
            //TODO: Error - should match
        }

        final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
        
        if (getEventHandler() != null) {
            getEventHandler().valueNull(key);
        }
        awaitingKey = true;
        setKeyBit(true);

    }

    @Override
    public void notifyMapStart() {
        objectStack.push(JSONValueType.OBJECT);
        setKeyBit(true);
        awaitingKey = true;
        if (getEventHandler() != null) {
            getEventHandler().jsonObjectStart(keyStack.peek());
        }

    }

    @Override
    public void notifyMapEnd() {
        if (objectStack.pop() != JSONValueType.OBJECT) {
            //TODO: Error should be object
        }
        final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : (keyStack.size() > 0 ? keyStack.pop() : "$");
        if (getEventHandler() != null) {
            getEventHandler().jsonObjectEnd(key);
        }
        
        
    }

    @Override
    public void notifyArrayStart() {
        objectStack.push(JSONValueType.ARRAY);
        if (getEventHandler() != null) {
            getEventHandler().jsonArrayStart(keyStack.peek());
        }
    }

    @Override
    public void notifyArrayEnd() {
        if (objectStack.pop() != JSONValueType.ARRAY) {
            //TODO: Error should be object
        }
        final String key = objectStack.peek() == JSONValueType.ARRAY ? keyStack.peek() : keyStack.pop();
        if (getEventHandler() != null) {
            getEventHandler().jsonArrayEnd(key);
        }

    }

    @Override
    public void notifyEntityEnd() {
        if (objectStack.peek() == JSONValueType.OBJECT) {
            awaitingKey = true;
            setKeyBit(true);
        }
    }
    
    @Override
    public void notifyKeyEnd() {
        awaitingKey = false;
        setKeyBit(false);
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

    @Override
    public void notifyEvent(JSONEvent event) {
        // TODO Auto-generated method stub
        
    }

}
