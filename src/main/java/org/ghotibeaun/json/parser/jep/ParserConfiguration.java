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
package org.ghotibeaun.json.parser.jep;

import org.ghotibeaun.json.parser.jep.eventhandler.JSONEventHandler;
import org.ghotibeaun.json.parser.jep.eventprovider.EventProvider;
import org.ghotibeaun.json.parser.jep.eventprovider.JSONEventProvider;
import org.ghotibeaun.json.parser.jep.processor.EventProcessor;
import org.ghotibeaun.json.parser.jep.processor.JSONEventProcessor;

public class ParserConfiguration {

    private JSONEventHandler handler;
    private JSONEventProvider provider;
    private JSONEventProcessor processor;

    private ParserConfiguration() {
        // TODO Auto-generated constructor stub
    }


    private void setEventHandler(JSONEventHandler handler) {
        this.handler = handler;
    }

    public JSONEventHandler getEventHandler() {
        return handler;
    }

    private void setEventProvider(JSONEventProvider provider) {
        this.provider = provider;
    }

    public JSONEventProvider getEventProvider() {
        return provider;
    }

    private void setEventProcessor(JSONEventProcessor processor) {
        this.processor = processor;
    }

    public JSONEventProcessor getEventProcessor() {
        return processor;
    }


    public static ParserConfiguration newConfiguration(JSONEventHandler eventHandler) {
        final ParserConfiguration pc = new ParserConfiguration();
        pc.setEventHandler(eventHandler);
        pc.setEventProvider(getDefaultEventProvider());
        pc.setEventProcessor(getDefaultEventProcessor());
        return pc;
    }


    public static ParserConfiguration newConfiguration(JSONEventHandler eventHandler, JSONEventProvider provider) {
        final ParserConfiguration pc = new ParserConfiguration();
        pc.setEventHandler(eventHandler);
        pc.setEventProvider(provider);
        pc.setEventProcessor(getDefaultEventProcessor());
        return pc;
    }

    public static ParserConfiguration newConfiguration(JSONEventHandler eventHandler, JSONEventProcessor processor, JSONEventProvider provider) {
        final ParserConfiguration pc = new ParserConfiguration();
        pc.setEventHandler(eventHandler);
        pc.setEventProcessor(processor);
        pc.setEventProvider(provider);
        return pc;
    }


    private static JSONEventProcessor getDefaultEventProcessor() {

        return EventProcessor.newDefaultProcessor();
    }

    private static JSONEventProvider getDefaultEventProvider() {
        return EventProvider.newDefaultEventProvider();
    }

    public void init() {

    }

}
