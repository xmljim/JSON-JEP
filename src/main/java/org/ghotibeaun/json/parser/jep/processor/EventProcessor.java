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
