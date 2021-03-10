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
