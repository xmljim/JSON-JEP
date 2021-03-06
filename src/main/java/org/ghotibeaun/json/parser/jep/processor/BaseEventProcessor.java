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
package org.ghotibeaun.json.parser.jep.processor;

import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.ARRAY_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.ARRAY_START;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.BOOLEAN_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.BOOLEAN_START;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.DOCUMENT_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.DOCUMENT_START;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.ENTITY_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.KEY_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.NULL_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.NULL_START;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.NUMBER_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.NUMBER_START;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.OBJECT_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.OBJECT_START;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.STRING_END;
import static org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEventType.STRING_START;

import java.nio.ByteBuffer;

import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public abstract class BaseEventProcessor extends EventProcessor {

    public BaseEventProcessor() {

    }

    protected void fireArrayEndEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(ARRAY_END));
    }

    protected void fireArrayEndEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(ARRAY_END, lineNumber, column));
    }

    protected void fireArrayStartEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(ARRAY_START));
    }

    protected void fireArrayStartEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(ARRAY_START, lineNumber, column));
    }

    protected void fireBooleanStartEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(BOOLEAN_START));
    }

    protected void fireBooleanStartEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(BOOLEAN_START, lineNumber, column));
    }

    protected void fireBooleanEndEvent(ByteBuffer data) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, BOOLEAN_END));
    }

    protected void fireBooleanEndEvent(ByteBuffer data, int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, BOOLEAN_END, lineNumber, column));
    }

    protected void fireDocumentEndEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(DOCUMENT_END));
    }

    protected void fireDocumentEndEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(DOCUMENT_END, lineNumber, column));
    }

    protected void fireDocumentStartEvent(ByteBuffer data) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, DOCUMENT_START));
    }

    protected void fireDocumentStartEvent(ByteBuffer data, int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, DOCUMENT_START, lineNumber, column));
    }

    protected void fireEntityEndEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(ENTITY_END));
    }

    protected void fireEntityEndEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(ENTITY_END, lineNumber, column));
    }

    protected void fireKeyEndEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(KEY_END));
    }

    protected void fireKeyEndEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(KEY_END, lineNumber, column));
    }

    protected void fireMapEndEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(OBJECT_END));
    }

    protected void fireMapEndEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(OBJECT_END, lineNumber, column));
    }

    protected void fireMapStartEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(OBJECT_START));
    }

    protected void fireMapStartEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(OBJECT_START, lineNumber, column));
    }

    protected void fireNullEndEvent(ByteBuffer data) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, NULL_END));
    }

    protected void fireNullEndEvent(ByteBuffer data, int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, NULL_END, lineNumber, column));
    }

    protected void fireNullStartEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(NULL_START));
    }

    protected void fireNullStartEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(NULL_START, lineNumber, column));
    }

    protected void fireNumberStartEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(NUMBER_START));
    }

    protected void fireNumberStartEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(NUMBER_START, lineNumber, column));
    }

    protected void fireNumberEndEvent(ByteBuffer data) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, NUMBER_END));
    }

    protected void fireNumberEndEvent(ByteBuffer data, int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, NUMBER_END, lineNumber, column));
    }

    protected void fireStringEndEvent(ByteBuffer data) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, STRING_END));
    }

    protected void fireStringEndEvent(ByteBuffer data, int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newDataEvent(data, STRING_END, lineNumber, column));
    }

    protected void fireStringStartEvent() {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(STRING_START));
    }

    protected void fireStringStartEvent(int lineNumber, int column) {
        getEventProvider().notifyEvent(JSONEvent.newTokenEvent(STRING_START, lineNumber, column));
    }
}
