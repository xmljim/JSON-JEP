package org.ghotibeaun.json.parser.jep;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.exception.JSONEventParserException;

public interface JSONEventParser {


    void parse(String data, ParserSettings settings) throws JSONEventParserException;

    void parse(InputStream inputStream, ParserSettings settings) throws JSONEventParserException;

    void parse(Path path, ParserSettings settings) throws JSONEventParserException;

    void parse(File file, ParserSettings settings) throws JSONEventParserException;

    void parse(URL url, ParserSettings settings) throws JSONEventParserException;



}
