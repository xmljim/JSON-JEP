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

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventhandler.JSONEventHandler;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public abstract class EventProvider implements JSONEventProvider {

    private ParserSettings settings;

    public EventProvider() {

    }

    @Override
    public ParserSettings getParserSettings() {
        return settings;
    }

    @Override
    public void setParserSettings(ParserSettings settings) {
        this.settings = settings;

    }

    public JSONEventHandler getEventHandler() {
        return settings.getParserConfiguration().getEventHandler();
    }

    public static EventProvider newDefaultEventProvider() {
        //return new DefaultEventProvider();
        return FactorySettings.createFactoryClass(Setting.EVENT_PROVIDER_CLASS);
    }


    public abstract void notifyDocumentStart(ByteBuffer start);


    public abstract void notifyDocumentEnd();


    public abstract void notifyStringTokenStart();


    public abstract void notifyStringTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyBooleanTokenStart();


    public abstract void notifyBooleanTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyNumberTokenStart();


    public abstract void notifyNumberTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyNullTokenStart();


    public abstract void notifyNullTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyMapStart();


    public abstract void notifyMapEnd();


    public abstract void notifyKeyEnd();

    /**
     * Notify consumer that we encountered the start of an array boundary
     */

    public abstract void notifyArrayStart();

    /**
     * Notify consumer that we encountered the end of an array boundary
     */

    public abstract void notifyArrayEnd();

    /**
     * Notify consumer that we encountered an entity terminator (e.g.,) a comma
     */

    public abstract void notifyEntityEnd();

    @Override
    public abstract void notifyEvent(JSONEvent event);

}
