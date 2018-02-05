package org.ghotibeaun.json.parser.jep.processor;

import java.io.InputStream;

import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.parser.jep.Configurable;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventprovider.JSONEventProvider;

public interface JSONEventProcessor extends Configurable {

    
    JSONEventProvider getEventProvider();
    
    ParserSettings getProcessorSettings();

    void start(InputStream stream) throws JSONEventParserException;
}
