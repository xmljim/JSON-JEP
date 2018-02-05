package org.ghotibeaun.json.parser.jep;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.exception.JSONEventParserException;

public abstract class EventParser implements JSONEventParser {

    public static EventParser newEventParser() {
        return new JSONEventParserImpl();
    }
    
    @Override
    public abstract void parse(String data, ParserSettings settings) throws JSONEventParserException;
    
    @Override
    public abstract void parse(InputStream inputStream, ParserSettings settings) throws JSONEventParserException;
    
    @Override
    public abstract void parse(Path path, ParserSettings settings) throws JSONEventParserException;
    
    @Override
    public abstract void parse(File file, ParserSettings settings) throws JSONEventParserException;
    
    @Override
    public abstract void parse(URL url, ParserSettings settings) throws JSONEventParserException;
    
}
