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

import java.io.InputStream;

import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventprovider.JSONEventProvider;



public abstract class EventProcessor implements JSONEventProcessor {

    private JSONEventProvider provider;
    private ParserSettings settings;

    public EventProcessor() {

    }

    public static EventProcessor newDefaultProcessor() {
        //return new JSONBufferedEventProcessor();
        return FactorySettings.createFactoryClass(Setting.EVENT_PROCESSOR_CLASS);
    }

    @Override
    public void setParserSettings(ParserSettings settings) {
        this.settings = settings;
        provider = settings.getParserConfiguration().getEventProvider();

    }

    @Override
    public ParserSettings getParserSettings() {
        // TODO Auto-generated method stub
        return settings;
    }

    @Override
    public abstract void start(InputStream stream) throws JSONEventParserException;

    @Override
    public JSONEventProvider getEventProvider() {
        return provider;
    }

    @Override
    public ParserSettings getProcessorSettings() {
        return settings;
    }

}
