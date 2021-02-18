package org.ghotibeaun.json.parser.jep;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

public abstract class EventParser implements JSONEventParser {

    public static EventParser newEventParser() {
        return FactorySettings.createFactoryClass(Setting.EVENT_PARSER_CLASS);
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
