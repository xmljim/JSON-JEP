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
