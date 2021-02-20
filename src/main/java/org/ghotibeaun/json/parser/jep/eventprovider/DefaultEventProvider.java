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
